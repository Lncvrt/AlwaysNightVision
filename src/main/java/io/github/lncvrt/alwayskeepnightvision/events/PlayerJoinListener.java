package io.github.lncvrt.alwayskeepnightvision.events;

import io.github.lncvrt.alwayskeepnightvision.AlwaysNightVision;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final AlwaysNightVision plugin;

    public PlayerJoinListener(AlwaysNightVision plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.applyNightVision(event.getPlayer());
    }
}
