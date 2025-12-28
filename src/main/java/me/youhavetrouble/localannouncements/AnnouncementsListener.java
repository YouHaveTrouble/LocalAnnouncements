package me.youhavetrouble.localannouncements;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class AnnouncementsListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (LocalAnnouncements.getPluginConfig().joinMessageRadius < 0) return;
        Component joinMessage = event.joinMessage();
        event.joinMessage(null);
        if (joinMessage == null) return;
        event.getPlayer()
                .getLocation()
                .getNearbyPlayers(LocalAnnouncements.getPluginConfig().joinMessageRadius)
                .forEach(player -> player.sendMessage(joinMessage));
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (LocalAnnouncements.getPluginConfig().quitMessageRadius < 0) return;
        Component quitMessage = event.quitMessage();
        event.quitMessage(null);
        if (quitMessage == null) return;
        event.getPlayer()
                .getLocation()
                .getNearbyPlayers(LocalAnnouncements.getPluginConfig().quitMessageRadius)
                .forEach(player -> player.sendMessage(quitMessage));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        double radius = LocalAnnouncements.getPluginConfig().deathMessageRadius;
        if (radius < 0) {
            Bukkit.getOnlinePlayers().forEach(this::playPlayerDeathSound);
            return;
        }
        Component deathMessage = event.deathMessage();
        event.deathMessage(null);
        if (deathMessage == null) return;
        event.getEntity()
                .getLocation()
                .getNearbyPlayers(radius)
                .forEach(player -> {
                    player.sendMessage(deathMessage);
                    playPlayerDeathSound(player);
                });
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        double radius = LocalAnnouncements.getPluginConfig().advancementMessageRadius;
        if (radius < 0) return;
        Component message = event.message();
        if (message == null) return;
        event.getPlayer().getLocation().getNearbyPlayers(radius).forEach(player -> player.sendMessage(message));
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent event) {
        double radius = LocalAnnouncements.getPluginConfig().chatMessageRadius;
        if (radius < 0) return;
        event.viewers().removeIf(audience -> {
            if (!(audience instanceof Player player)) return false;
            return !(player.getLocation().distanceSquared(event.getPlayer().getLocation()) <= radius * radius);
        });
    }


    private void playPlayerDeathSound(@NotNull Player player) {
        if (LocalAnnouncements.getPluginConfig().playerDeathSoundKey == null) return;
        Sound sound = Sound.sound(
                LocalAnnouncements.getPluginConfig().playerDeathSoundKey,
                Sound.Source.PLAYER,
                LocalAnnouncements.getPluginConfig().playerDeathSoundVolume,
                1.0f
        );
        player.playSound(sound);
    }

}
