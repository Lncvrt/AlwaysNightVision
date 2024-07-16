package io.github.lncvrt.alwaysnightvision;

import com.google.gson.reflect.TypeToken;
import io.github.lncvrt.alwaysnightvision.commands.ToggleNightVision;
import io.github.lncvrt.alwaysnightvision.events.PlayerJoin;
import io.github.lncvrt.alwaysnightvision.events.PlayerQuit;
import io.github.lncvrt.alwaysnightvision.events.PlayerRespawn;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public final class AlwaysNightVision extends JavaPlugin {
    public File dataFile;
    public File messagesFile;
    public File configFile;
    public FileConfiguration messagesConfig;
    public FileConfiguration mainConfig;
    public Map<UUID, Boolean> nightVisionStates;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onEnable() {
        try {
            loadResources();
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

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
            saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadResources() throws IOException {
        dataFile = new File(getDataFolder(), "data.json");
        configFile = new File(getDataFolder(), "config.yml");
        messagesFile = new File(getDataFolder(), "messages.yml");

        if (nightVisionStates == null) {
            nightVisionStates = new HashMap<>();
        }

        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            dataFile.createNewFile();
            saveData(); // save an empty "statuses" object
        } else {
            loadNightVisionStates();
        }

        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
        mainConfig = new YamlConfiguration();
        try {
            mainConfig.load(configFile);
            updateYamlFile(configFile, mainConfig);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        messagesConfig = new YamlConfiguration();
        try {
            messagesConfig.load(messagesFile);
            updateYamlFile(messagesFile, messagesConfig);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        getServer().getOnlinePlayers().forEach(this::removeNightVision);
        getServer().getOnlinePlayers().forEach(this::applyNightVision);

        getLogger().info("Successfully loaded resources!");
    }

    private void updateYamlFile(File file, FileConfiguration config) throws IOException {
        String pluginVersion = getDescription().getVersion();

        String configVersion = config.getString("config-version");
        if (configVersion == null || !configVersion.equals(pluginVersion)) {
            YamlConfiguration oldConfig = new YamlConfiguration();
            try {
                oldConfig.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            InputStream defaultConfigStream = getResource(file.getName());
            YamlConfiguration defaultConfig = new YamlConfiguration();
            try {
                defaultConfig.load(new InputStreamReader(defaultConfigStream));
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            for (String key : defaultConfig.getKeys(true)) {
                if (oldConfig.contains(key)) {
                    defaultConfig.set(key, oldConfig.get(key));
                }
            }

            defaultConfig.set("config-version", pluginVersion);
            defaultConfig.save(file);

            if (file.getName().equals("config.yml")) {
                mainConfig = defaultConfig;
            } else if (file.getName().equals("messages.yml")) {
                messagesConfig = defaultConfig;
            }

            if (configVersion == null) {
                configVersion = "n/a (Legacy)";
            }

            getLogger().info("Updated " + file.getName() + " from version " + configVersion + " to version " + pluginVersion);
        }
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
        try (FileReader reader = new FileReader(dataFile)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            if (jsonObject != null && jsonObject.has("statuses")) {
                JsonObject statuses = jsonObject.getAsJsonObject("statuses");
                nightVisionStates = gson.fromJson(statuses, new TypeToken<Map<UUID, Boolean>>(){}.getType());
            } else {
                nightVisionStates = new HashMap<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("statuses", gson.toJsonTree(nightVisionStates));
        try (FileWriter writer = new FileWriter(dataFile)) {
            gson.toJson(jsonObject, writer);
        }
    }

    public String getMessage(String messageName, Player player) {
        String message = messagesConfig.getString(messageName);
        if (message == null) {
            message = "&c&lFailed to parse/read message \"" + messageName + "\".";
        }

        if (player != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        return translateAlternateColorCodes('&', message.replace("\\n", "\n"));
    }

    public boolean getConfigBoolean(String configName) {
        return mainConfig.getBoolean(configName);
    }

    public String getConfigString(String configName) {
        return mainConfig.getString(configName);
    }
}
