package me.youhavetrouble.localannouncements;

public class LAConfig {

    public final double joinMessageRadius;
    public final double quitMessageRadius;
    public final double deathMessageRadius;
    public final double advancementMessageRadius;
    public final double chatMessageRadius;


    public LAConfig(LocalAnnouncements plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        joinMessageRadius = plugin.getConfig().getDouble("announcement-range.join", 256.0);
        quitMessageRadius = plugin.getConfig().getDouble("announcement-range.quit", 256.0);
        deathMessageRadius = plugin.getConfig().getDouble("announcement-range.death", 256.0);
        advancementMessageRadius = plugin.getConfig().getDouble("announcement-range.advancement", 256.0);
        chatMessageRadius = plugin.getConfig().getDouble("announcement-range.chat", -1);
    }

}
