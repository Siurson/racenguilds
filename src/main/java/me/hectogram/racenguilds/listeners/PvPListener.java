package me.hectogram.racenguilds.listeners;

import me.hectogram.racenguilds.PvPManager;
import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvPListener implements Listener {

    private final PvPManager pvpManager;
    private final RaceManager raceManager;

    public PvPListener(PvPManager pvpManager, RaceManager raceManager) {
        this.pvpManager = pvpManager;
        this.raceManager = raceManager;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        RaceType attackerRace = raceManager.getRace(attacker);
        RaceType victimRace = raceManager.getRace(victim);


        if (attackerRace == RaceType.GOBLIN) return;


        if (victimRace == RaceType.GOBLIN) return;


        if (!pvpManager.isPvPEnabled()) {
            event.setCancelled(true);
            attacker.sendMessage("§cNie ma obecnie stanu wojny! Tylko Goblin może atakować w tym czasie.");
        }
    }

}
