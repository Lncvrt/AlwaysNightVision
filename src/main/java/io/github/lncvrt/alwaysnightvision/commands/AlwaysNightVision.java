package io.github.lncvrt.alwaysnightvision.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class AlwaysNightVision implements CommandExecutor {
    private final io.github.lncvrt.alwaysnightvision.AlwaysNightVision plugin;

    public AlwaysNightVision(io.github.lncvrt.alwaysnightvision.AlwaysNightVision plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            plugin.loadResources();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }
        sender.sendMessage(plugin.getMessage("reload-message", null));
        return true;
    }
}
