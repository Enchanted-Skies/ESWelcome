package me.xemor.eswelcome;

import static me.xemor.eswelcome.ESJoin.getPrefix;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ESWelcomeCommands implements CommandExecutor, TabExecutor {

    ESWelcome esWelcome;
    public ESWelcomeCommands(ESWelcome esWelcome) {
        this.esWelcome = esWelcome;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("eswelcome.admin")) {
            if (args.length == 0) {
                final BaseComponent[] messages = new BaseComponent[getPrefix().length + 1];
                messages[getPrefix().length]= new TextComponent("You need to put an argument! Example: reload.");
                sender.spigot().sendMessage(messages);
            }
            if (args[0].equals("reload")) {
                esWelcome.reload();
                final BaseComponent[] messages = new BaseComponent[getPrefix().length + 1];
                messages[getPrefix().length] = new TextComponent("ESWelcome has been successfully reloaded!");
                sender.spigot().sendMessage(messages);
                // send a message to the console as well
                // IGNORE LINT, type erasure fails compile time if replaced with method reference
                Bukkit.getConsoleSender().sendMessage(Arrays.stream(messages).map(it -> it.toPlainText()).toArray(String[]::new));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.singletonList("reload");
    }

}
