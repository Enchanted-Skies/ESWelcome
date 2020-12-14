package me.xemor.eswelcome;

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

    private Plugin plugin;
    String firstTimeWelcomeMessage;
    String welcomeMessage;
    List<String> firstTimePersonalMessages;
    List<String> personalMessages;

    public ESJoin(ESWelcome pl) {
        plugin = pl; //Set Class's Variable plugin to pl
        loadConfig();
    }

    public void loadConfig() {
        welcomeMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("welcomeMessage"));
        firstTimeWelcomeMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("firstTimeWelcomeMessage"));
        personalMessages = plugin.getConfig().getStringList("personalMessages");
        personalMessages = chatColorify(personalMessages);
        firstTimePersonalMessages = plugin.getConfig().getStringList("firstTimePersonalMessages");
        firstTimePersonalMessages = chatColorify(firstTimePersonalMessages);
    }

    public List<String> chatColorify(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            String colorifed = ChatColor.translateAlternateColorCodes('&', list.get(i));
            list.set(i, colorifed);
        }
        return list;
    }

    @EventHandler(priority = EventPriority.HIGHEST) //onJoin do ...
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer(); //Make Player variable
        int offlinePlayers = Bukkit.getServer().getOfflinePlayers().length;
        int playerCount = offlinePlayers;
        if (!player.hasPlayedBefore()) { //If the player hasn't played before
            String tempWelcomeMessage = firstTimeWelcomeMessage;
            tempWelcomeMessage = tempWelcomeMessage.replace("{PLAYER}", player.getName());
            tempWelcomeMessage = tempWelcomeMessage.replace("{COUNT}", Integer.toString(playerCount));
            e.setJoinMessage(tempWelcomeMessage);
            for (String personalMessage : firstTimePersonalMessages) {
                personalMessage = personalMessage.replace("{PLAYER}", player.getName());
                personalMessage = personalMessage.replace("{COUNT}", Integer.toString(playerCount));
                player.sendMessage(personalMessage);
            }
        }
        else {
            String tempWelcome = welcomeMessage;
            tempWelcome = tempWelcome.replace("{PLAYER}", player.getName());
            e.setJoinMessage(tempWelcome);
            for (String personalMessage : personalMessages) {
                String tempPersonalMessage = personalMessage.replace("{PLAYER}", player.getName());
                player.sendMessage(tempPersonalMessage);
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }
}
