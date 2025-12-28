package me.youhavetrouble.localannouncements;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class LocalAnnouncements extends JavaPlugin {

    private static LAConfig config;

    private final Permission pluginReloadPermission = new Permission(
            "localannouncements.reload",
            "Allows reloading plugin config",
            PermissionDefault.OP
    );

    @Override
    public void onEnable() {
        reloadPluginConfig();
        getServer().getPluginManager().registerEvents(new AnnouncementsListener(), this);

        LifecycleEventManager<@NotNull Plugin> manager = getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(
                    Commands.literal("localannouncements").then(
                            Commands.literal("reload")
                                    .requires(css -> {
                                        if (!(css.getExecutor() instanceof Permissible permissible)) return false;
                                        return permissible.hasPermission(pluginReloadPermission);
                                    })
                                    .executes(css -> {
                                        reloadPluginConfig();
                                        css.getSource().getSender().sendRichMessage("<green>LocalAnnouncements configuration reloaded.");
                                        return Command.SINGLE_SUCCESS;
                                    })
                                    .build()
                    ).build()
            );
        });
    }

    public void reloadPluginConfig() {
        config = new LAConfig(this);
    }

    public static LAConfig getPluginConfig() {
        return config;
    }

}
