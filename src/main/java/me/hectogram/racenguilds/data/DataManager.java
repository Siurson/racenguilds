package me.hectogram.racenguilds.data;

import me.hectogram.racenguilds.guild.GuildRole;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class DataManager {

    private final File file;
    private final FileConfiguration config;

    public DataManager(JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "players.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    //rasa
    public void saveRace(UUID uuid, RaceType race) {
        config.set(uuid.toString() + ".race", race.name());
        save();
    }

    public RaceType loadRace(UUID uuid) {
        if (!config.contains(uuid.toString() + ".race")) return null;

        String value = config.getString(uuid.toString() + ".race");
        try {
            return RaceType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void deleteRace(UUID uuid) {
        if (!config.contains(uuid.toString() + ".race")) return;

        config.set(uuid.toString() + ".race", null); // usuń tylko race
        save();
    }

    // rpla
    public void saveGuildRole(UUID uuid, GuildRole role) {
        config.set("roles." + uuid.toString(), role.name());
        save();
    }

    public GuildRole loadGuildRole(UUID uuid) {
        String raw = config.getString("roles." + uuid.toString());
        if (raw == null) return null;

        try {
            return GuildRole.valueOf(raw);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void deleteGuildRole(UUID uuid) {
        config.set("roles." + uuid.toString(), null);
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
