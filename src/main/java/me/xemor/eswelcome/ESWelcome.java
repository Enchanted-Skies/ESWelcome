package me.xemor.eswelcome;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ESWelcome extends JavaPlugin {

    private ESJoin esJoin;
    private static BukkitAudiences bukkitAudiences;


    @Override
    public void onEnable() {
        bukkitAudiences = BukkitAudiences.create(this);
        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();
        esJoin = new ESJoin(this);
        PluginCommand command = this.getCommand("eswelcome");
        ESWelcomeCommands esWelcomeCommands = new ESWelcomeCommands(this);
        command.setTabCompleter(esWelcomeCommands);
        command.setExecutor(esWelcomeCommands);
        registerEvents(this, esJoin);
    }

    public void reload() {
        reloadConfig();
        esJoin.loadConfig();
    }

    //Much easier then registering events in 10 different methods
    public static void registerEvents(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public static BukkitAudiences getBukkitAudiences() {
        return bukkitAudiences;
    }
}
