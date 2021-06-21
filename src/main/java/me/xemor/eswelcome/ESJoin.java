package me.xemor.eswelcome;

import de.themoep.minedown.adventure.MineDown;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ESJoin implements Listener {

    private final ESWelcome plugin;
    String firstTimeWelcomeMessage;
    String welcomeMessage;
    List<String> firstTimePersonalMessages;
    List<String> personalMessages;

    public ESJoin(ESWelcome pl) {
        plugin = pl;
        loadConfig();
    }

    public void loadConfig() {
        welcomeMessage = plugin.getConfig().getString("welcomeMessage");
        firstTimeWelcomeMessage = plugin.getConfig().getString("firstTimeWelcomeMessage");
        personalMessages = plugin.getConfig().getStringList("personalMessages");
        firstTimePersonalMessages = plugin.getConfig().getStringList("firstTimePersonalMessages");
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
        }
        else {
            ESWelcome.getBukkitAudiences().players().sendMessage(parse(player, welcomeMessage));
            for (String personalMessage : personalMessages) {
                ESWelcome.getBukkitAudiences().player(player).sendMessage(parse(player, personalMessage));
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
        return new MineDown(placeholderFirstTime).replaceFirst(true).replace("count", Integer.toString(numberOfPlayers)).toComponent();
    }
}
