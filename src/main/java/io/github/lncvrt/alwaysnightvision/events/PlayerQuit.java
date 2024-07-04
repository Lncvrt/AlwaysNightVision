package io.github.lncvrt.alwaysnightvision.events;

import io.github.lncvrt.alwaysnightvision.AlwaysNightVision;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    private final AlwaysNightVision plugin;

    public PlayerQuit(AlwaysNightVision plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.removeNightVision(event.getPlayer());
    }
}
