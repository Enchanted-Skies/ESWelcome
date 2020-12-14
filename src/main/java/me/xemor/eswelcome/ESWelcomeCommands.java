package me.xemor.eswelcome;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Arrays;
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
                sender.sendMessage("You need to put an argument! Example: reload.");
            }
            if (args[0].equals("reload")) {
                esWelcome.reload();
                sender.sendMessage("It has been successfully reloaded!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Arrays.asList(new String[]{"reload"});
    }

}
