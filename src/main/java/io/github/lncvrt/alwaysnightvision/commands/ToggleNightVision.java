package io.github.lncvrt.alwaysnightvision.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ToggleNightVision implements CommandExecutor {
    private final io.github.lncvrt.alwaysnightvision.AlwaysNightVision plugin;

    public ToggleNightVision(io.github.lncvrt.alwaysnightvision.AlwaysNightVision plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            boolean currentState = plugin.nightVisionStates.getOrDefault(uuid, true);
            boolean newState = !currentState;
            plugin.nightVisionStates.put(uuid, newState);
            if (newState) {
                plugin.applyNightVision(player);
                player.sendMessage(plugin.getMessage("enable-message", player));
            } else {
                plugin.removeNightVision(player);
                player.sendMessage(plugin.getMessage("disable-message", player));
            }
            return true;
        }
        return false;
    }
}
