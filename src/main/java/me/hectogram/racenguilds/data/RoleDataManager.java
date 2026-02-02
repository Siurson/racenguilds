package me.hectogram.racenguilds.data;

import me.hectogram.racenguilds.guild.GuildRole;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class RoleDataManager {

    private final File file;
    private final FileConfiguration config;

    public RoleDataManager(JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "roles.yml");

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveRole(UUID uuid, GuildRole role) {
        config.set(uuid.toString(), role.name());
        save();
    }

    public GuildRole loadRole(UUID uuid) {
        String raw = config.getString(uuid.toString());
        Bukkit.getLogger().info("Loading role for " + uuid + ": " + raw);
        if (raw == null) return null;
        return GuildRole.valueOf(raw);
    }


    public void deleteRole(UUID uuid) {
        config.set(uuid.toString(), null);
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
