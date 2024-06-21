package io.github.lncvrt.alwayskeepnightvision.events;

import io.github.lncvrt.alwayskeepnightvision.AlwaysNightVision;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
    private final AlwaysNightVision plugin;

    public PlayerRespawnListener(AlwaysNightVision plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        plugin.applyNightVision(event.getPlayer());
    }
}
