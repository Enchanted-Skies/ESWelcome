package me.xemor.eswelcome;

import de.themoep.minedown.adventure.MineDown;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class ESJoin implements Listener {

    private final ESWelcome plugin;
    String firstTimeWelcomeMessage;
    String welcomeMessage;
    List<String> firstTimePersonalMessages;
    List<String> personalMessages;
    List<String> firstTimeCommands;
    List<String> commands;

    public ESJoin(ESWelcome pl) {
        plugin = pl;
        loadConfig();
    }

    public void loadConfig() {
        welcomeMessage = plugin.getConfig().getString("welcomeMessage");
        firstTimeWelcomeMessage = plugin.getConfig().getString("firstTimeWelcomeMessage");
        personalMessages = plugin.getConfig().getStringList("personalMessages");
        firstTimePersonalMessages = plugin.getConfig().getStringList("firstTimePersonalMessages");
        firstTimeCommands = plugin.getConfig().getStringList("firstTimeCommands");
        commands = plugin.getConfig().getStringList("commands");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(null);
        if (!player.hasPlayedBefore()) {
            ESWelcome.getBukkitAudiences().players().sendMessage(parse(player, firstTimeWelcomeMessage));
            for (String personalMessage : firstTimePersonalMessages) {
                ESWelcome.getBukkitAudiences().player(player).sendMessage(parse(player, personalMessage));
            }
            for (String firstTimeCommand : firstTimeCommands) {
                String replacedCommand = firstTimeCommand.replaceAll("%player_name%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replacedCommand);
            }
        }
        else {
            ESWelcome.getBukkitAudiences().players().sendMessage(parse(player, welcomeMessage));
            for (String personalMessage : personalMessages) {
                ESWelcome.getBukkitAudiences().player(player).sendMessage(parse(player, personalMessage));
            }
            for (String command : commands) {
                String replacedCommand = command.replaceAll("%player_name%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replacedCommand); //Consider moving into next tick
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }

    public Component parse(Player player, String message) {
        String placeholderFirstTime = PlaceholderAPI.setPlaceholders(player, message);
        int numberOfPlayers = Bukkit.getServer().getOfflinePlayers().length;
        return LegacyComponentSerializer.legacyAmpersand().deserialize(placeholderFirstTime.replaceAll("%count%", Integer.toString(numberOfPlayers)));
    }
}
