package io.github.lncvrt.alwaysnightvision;

import io.github.lncvrt.alwaysnightvision.commands.ToggleNightVision;
import io.github.lncvrt.alwaysnightvision.events.PlayerQuit;
import io.github.lncvrt.alwaysnightvision.events.PlayerRespawn;
import io.github.lncvrt.alwaysnightvision.events.PlayerJoin;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class AlwaysNightVision extends JavaPlugin {
    private File dataFile;
    private FileConfiguration dataConfig;
    private FileConfiguration messagesConfig;
    public Map<UUID, Boolean> nightVisionStates;

    @Override
    public void onEnable() {
        loadResources();

        getServer().getOnlinePlayers().forEach(this::applyNightVision);

        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawn(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);

        getCommand("togglenightvision").setExecutor(new ToggleNightVision(this));
        getCommand("alwaysnightvision").setExecutor(new io.github.lncvrt.alwaysnightvision.commands.AlwaysNightVision(this));
    }

    @Override
    public void onDisable() {
        getServer().getOnlinePlayers().forEach(this::applyNightVision);
        try {
            saveNightVisionStates();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadResources() {
        nightVisionStates = new HashMap<>();
        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        loadNightVisionStates();

        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

        getLogger().info("Successfully loaded resources!");
    }

    public void applyNightVision(Player player) {
        if (nightVisionStates.getOrDefault(player.getUniqueId(), true)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
        } else {
            removeNightVision(player);
        }
    }

    public void removeNightVision(Player player) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    private void loadNightVisionStates() {
        for (String key : dataConfig.getKeys(false)) {
            nightVisionStates.put(UUID.fromString(key), dataConfig.getBoolean(key));
        }
    }

    public void saveNightVisionStates() throws IOException {
        for (Map.Entry<UUID, Boolean> entry : nightVisionStates.entrySet()) {
            dataConfig.set(entry.getKey().toString(), entry.getValue());
        }
        dataConfig.save(dataFile);
    }

    public String getMessage(String messageName, Player player) {
        String message = messagesConfig.getString(messageName);
        if (message == null) {
            message = "&c&lFailed to parse/read message \"" + messageName + "\".";
        }

        if (player != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
