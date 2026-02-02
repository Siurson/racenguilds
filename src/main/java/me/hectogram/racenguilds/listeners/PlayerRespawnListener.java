package me.hectogram.racenguilds.listeners;

import me.hectogram.racenguilds.Racenguilds;
import me.hectogram.racenguilds.plague.PlagueManager;
import me.hectogram.racenguilds.race.RaceEffectsManager;
import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.entity.Player;

public class PlayerRespawnListener implements Listener {

    private final RaceManager raceManager;
    private final RaceEffectsManager effectsManager;
    private final Racenguilds plugin;
    private final PlagueManager plagueManager;

    public PlayerRespawnListener(RaceManager raceManager, RaceEffectsManager effectsManager, Racenguilds plugin, PlagueManager plagueManager) {
        this.raceManager = raceManager;
        this.effectsManager = effectsManager;
        this.plugin = plugin;
        this.plagueManager = plagueManager;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        RaceType race = raceManager.getRace(player);
        effectsManager.beginRaceSetup(player);

        effectsManager.applyPerks(player, race);
        if (race != null) {

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                effectsManager.applyPerks(player, race);
                if (plagueManager.isInfected(player)) {
                    plagueManager.applyPlagueEffects(player);
                }
            }, 1L);
        }
    }
}
