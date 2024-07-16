package io.github.lncvrt.alwaysnightvision.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

import static org.bukkit.ChatColor.RED;

public class AlwaysNightVision implements CommandExecutor {
    private final io.github.lncvrt.alwaysnightvision.AlwaysNightVision plugin;

    public AlwaysNightVision(io.github.lncvrt.alwaysnightvision.AlwaysNightVision plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(RED + "Invalid command usage. Please provide a valid argument.");
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (args.length > 1 && args[1].equalsIgnoreCase("confirm")) {
                try {
                    plugin.loadResources();
                } catch (IOException e) {
                    e.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(plugin);
                    return false;
                }
                sender.sendMessage(plugin.getMessage("reload-message", null));
            } else {
                sender.sendMessage(plugin.getMessage("reload-warn-message", null));
            }
        } else if (args[0].equalsIgnoreCase("save")) {
            try {
                plugin.saveData();
            } catch (IOException e) {
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(plugin);
                return false;
            }
            sender.sendMessage(plugin.getMessage("save-data-message", null));
        } else {
            sender.sendMessage(RED + "Unknown command. Use 'reload' or 'save'.");
            return false;
        }

        return true;
    }
}
