package me.youhavetrouble.localannouncements;

import net.kyori.adventure.key.Key;

public class LAConfig {

    public final double joinMessageRadius;
    public final double quitMessageRadius;
    public final double deathMessageRadius;
    public final double advancementMessageRadius;
    public final double chatMessageRadius;

    public final Key playerDeathSoundKey;
    public final float playerDeathSoundVolume;

    public LAConfig(LocalAnnouncements plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        joinMessageRadius = plugin.getConfig().getDouble("announcement-range.join", 256.0);
        quitMessageRadius = plugin.getConfig().getDouble("announcement-range.quit", 256.0);
        deathMessageRadius = plugin.getConfig().getDouble("announcement-range.death", 256.0);
        advancementMessageRadius = plugin.getConfig().getDouble("announcement-range.advancement", 256.0);
        chatMessageRadius = plugin.getConfig().getDouble("announcement-range.chat", -1);

        if (plugin.getConfig().getBoolean("player-death-sound.enabled", false)) {
            String deathSoundString = plugin.getConfig().getString("player-death-sound.sound", "minecraft:entity.wither.spawn");
            playerDeathSoundKey = Key.key(deathSoundString);
            playerDeathSoundVolume = (float) plugin.getConfig().getDouble("player-death-sound.volume", 1.0);
        } else {
            playerDeathSoundKey = null;
            playerDeathSoundVolume = 0.0f;
        }

    }

}
