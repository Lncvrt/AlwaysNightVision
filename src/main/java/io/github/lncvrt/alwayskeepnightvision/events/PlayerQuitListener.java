package io.github.lncvrt.alwayskeepnightvision.events;

import io.github.lncvrt.alwayskeepnightvision.AlwaysNightVision;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final AlwaysNightVision plugin;

    public PlayerQuitListener(AlwaysNightVision plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.removeNightVision(event.getPlayer());
    }
}
