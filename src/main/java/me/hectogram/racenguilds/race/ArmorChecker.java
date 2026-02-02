package me.hectogram.racenguilds.race;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ArmorChecker {

    public ArmorChecker(JavaPlugin plugin, RaceManager raceManager, RaceEffectsManager effectsManager) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    RaceType race = raceManager.getRace(player);
                    if (race != null) {
                        effectsManager.validateArmor(player, race);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 100L);
    }
}
