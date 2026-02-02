package me.hectogram.racenguilds.listeners;

import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ElfBowListener implements Listener {

    private final RaceManager raceManager;

    public ElfBowListener(RaceManager raceManager) {
        this.raceManager = raceManager;
    }

    @EventHandler
    public void onArrowDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;

        RaceType race = raceManager.getRace(player);
        if (race != RaceType.ELF) return;

        //  50%
        double originalDamage = event.getDamage();
        event.setDamage(originalDamage * 1.5);
    }
}
