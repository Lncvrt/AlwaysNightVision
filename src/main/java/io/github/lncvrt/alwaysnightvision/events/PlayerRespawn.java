package io.github.lncvrt.alwaysnightvision.events;

import io.github.lncvrt.alwaysnightvision.AlwaysNightVision;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener {
    private final AlwaysNightVision plugin;

    public PlayerRespawn(AlwaysNightVision plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.applyNightVision(event.getPlayer()), 1L);
    }
}
