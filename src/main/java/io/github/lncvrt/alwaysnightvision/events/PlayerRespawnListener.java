package io.github.lncvrt.alwaysnightvision.events;

import io.github.lncvrt.alwaysnightvision.AlwaysNightVision;
import org.bukkit.Bukkit;
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
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.applyNightVision(event.getPlayer());
            }
        }, 1L);
    }
}
