package io.github.lncvrt.alwaysnightvision.events;

import io.github.lncvrt.alwaysnightvision.AlwaysNightVision;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private final AlwaysNightVision plugin;

    public PlayerJoin(AlwaysNightVision plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.applyNightVision(event.getPlayer());
    }
}
