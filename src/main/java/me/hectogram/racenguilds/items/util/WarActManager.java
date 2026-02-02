package me.hectogram.racenguilds.items.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WarActManager {

    private final File file;
    private final FileConfiguration config;

    public WarActManager(JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "war_acts.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);


        if (!config.contains("acts")) {
            config.createSection("acts");
            save();
        }
    }



    public void saveAct(UUID playerUUID, String playerName, List<String> pages) {
        String path = "acts." + playerUUID;

        config.set(path + ".player", playerName);
        config.set(path + ".pages", pages);
        config.set(path + ".timestamp", System.currentTimeMillis());

        save();
    }



    public Set<String> getAllActUUIDs() {
        ConfigurationSection section = config.getConfigurationSection("acts");
        if (section == null) return Collections.emptySet();
        return section.getKeys(false);
    }

    public String getPlayerName(String uuid) {
        return config.getString("acts." + uuid + ".player");
    }

    public List<String> getPages(String uuid) {
        return config.getStringList("acts." + uuid + ".pages");
    }

    public long getTimestamp(String uuid) {
        return config.getLong("acts." + uuid + ".timestamp");
    }



    public boolean removeAct(String uuid) {
        if (!config.contains("acts." + uuid)) return false;

        config.set("acts." + uuid, null);
        save();
        return true;
    }

    public void clearAll() {
        config.set("acts", null);
        config.createSection("acts");
        save();
    }



    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
