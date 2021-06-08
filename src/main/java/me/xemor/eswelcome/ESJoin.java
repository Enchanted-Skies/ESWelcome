package me.xemor.eswelcome;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import de.themoep.minedown.MineDown;

import java.util.Arrays;
import java.util.List;
import static java.util.Objects.requireNonNull;

public class ESJoin implements Listener {

    private final Plugin plugin;

    private String firstTimeWelcomeMessage;
    private String welcomeMessage;
    private List<String> firstTimePersonalMessages;
    private List<String> personalMessages;
    private static BaseComponent[] prefix;
    private int playerCount = Bukkit.getServer().getOfflinePlayers().length;


    public ESJoin(ESWelcome pl) {
        plugin = pl; //Set Class's Variable plugin to pl
        loadConfig();
    }

    public void loadConfig() { // Load all messages from the config + prefix
        prefix = MineDown.parse(requireNonNull(plugin.getConfig().getString("commandPrefix")));
        welcomeMessage = requireNonNull(plugin.getConfig().getString("welcomeMessage"));
        firstTimeWelcomeMessage = requireNonNull(plugin.getConfig().getString("firstTimeWelcomeMessage"));
        personalMessages = plugin.getConfig().getStringList("personalMessages");
        firstTimePersonalMessages = plugin.getConfig().getStringList("firstTimePersonalMessages");
    }

    //sets join message and possibly sends the player a personal message
    @EventHandler(priority = EventPriority.HIGHEST) //onJoin do ...
    public void onJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer(); //Make Player variable
        // override the join message
        e.setJoinMessage(null);
        if (!player.hasPlayedBefore()) { //If the player hasn't played before
            playerCount++;
            sendGeneralJoinMessage(firstTimeWelcomeMessage, player);
            sendPersonalJoinMessage(firstTimePersonalMessages,player);
        } else {
            sendGeneralJoinMessage(welcomeMessage, player);
            sendPersonalJoinMessage(personalMessages, player);
        }
    }

    private void sendPersonalJoinMessage(List<String> messages, Player player) {
        //For every personal message, minedown it, replace the placeholders, then send the Basecomponents to the player.
        messages.stream().map(
                it -> new MineDown(it).replaceFirst(true)
                        .replace("PLAYER", player.getDisplayName(), "COUNT", Integer.toString(playerCount))
                        .toComponent())
                .forEachOrdered(it -> player.spigot().sendMessage(it));
    }

    private void sendGeneralJoinMessage(String message, Player player) {
        // map message to basecomponent via minedown then send to player + console.
        final BaseComponent[] baseComponents = new MineDown(message).replaceFirst(true).replace("PLAYER", player.getDisplayName(), "COUNT", Integer.toString(playerCount)).toComponent();
        // IGNORE LINT, type erasure fails compile time if replaced with method reference
        Bukkit.getConsoleSender().sendMessage(Arrays.stream(baseComponents).map(it -> it.toPlainText()).toArray(String[]::new));
        Bukkit.getOnlinePlayers().forEach(it -> it.spigot().sendMessage(baseComponents));
    }

    public static BaseComponent[] getPrefix() {
        return prefix;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }
}
