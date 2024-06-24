package io.github.lncvrt.alwaysnightvision;

import io.github.lncvrt.alwaysnightvision.events.PlayerQuitListener;
import io.github.lncvrt.alwaysnightvision.events.PlayerRespawnListener;
import io.github.lncvrt.alwaysnightvision.events.PlayerJoinListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class AlwaysNightVision extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getOnlinePlayers().forEach(this::applyNightVision);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
    }

    @Override
    public void onDisable() {
        getServer().getOnlinePlayers().forEach(this::applyNightVision);
    }

    public void applyNightVision(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
    }

    public void removeNightVision(Player player) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }
}
