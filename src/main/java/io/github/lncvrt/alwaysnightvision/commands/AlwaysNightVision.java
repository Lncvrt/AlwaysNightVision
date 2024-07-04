package io.github.lncvrt.alwaysnightvision.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AlwaysNightVision implements CommandExecutor {
    private final io.github.lncvrt.alwaysnightvision.AlwaysNightVision plugin;

    public AlwaysNightVision(io.github.lncvrt.alwaysnightvision.AlwaysNightVision plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.loadResources();
        sender.sendMessage(plugin.getMessage("reload-message", null));
        return true;
    }
}
