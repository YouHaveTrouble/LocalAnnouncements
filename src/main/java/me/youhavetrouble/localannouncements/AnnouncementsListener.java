package me.youhavetrouble.localannouncements;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AnnouncementsListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (LocalAnnouncements.getPluginConfig().joinMessageRadius < 0) return;
        Component joinMessage = event.joinMessage();
        event.joinMessage(null);
        if (joinMessage == null) return;
        event.getPlayer().getLocation().getNearbyPlayers(LocalAnnouncements.getPluginConfig().joinMessageRadius).forEach(player -> {
            player.sendMessage(joinMessage);
        });
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (LocalAnnouncements.getPluginConfig().quitMessageRadius < 0) return;
        Component quitMessage = event.quitMessage();
        event.quitMessage(null);
        if (quitMessage == null) return;
        event.getPlayer().getLocation().getNearbyPlayers(LocalAnnouncements.getPluginConfig().quitMessageRadius).forEach(player -> {
            player.sendMessage(quitMessage);
        });
    }


    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        double radius = LocalAnnouncements.getPluginConfig().deathMessageRadius;
        if (radius < 0) return;
        Component deathMessage = event.deathMessage();
        event.deathMessage(null);
        if (deathMessage == null) return;
        event.getEntity().getLocation().getNearbyPlayers(radius).forEach(player -> {
            player.sendMessage(deathMessage);
        });
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        double radius = LocalAnnouncements.getPluginConfig().advancementMessageRadius;
        if (radius < 0) return;
        Component message = event.message();
        if (message == null) return;
        event.getPlayer().getLocation().getNearbyPlayers(radius).forEach(player -> {
            player.sendMessage(message);
        });
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

}
