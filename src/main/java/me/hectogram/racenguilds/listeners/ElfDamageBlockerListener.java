package me.hectogram.racenguilds.listeners;

import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Set;

public class ElfDamageBlockerListener implements Listener {

    private final RaceManager raceManager;
    private final Set<EntityType> blockedAnimals = Set.of(
            EntityType.COW,
            EntityType.PIG,
            EntityType.SHEEP,
            EntityType.CHICKEN,
            EntityType.RABBIT,
            EntityType.PANDA,
            EntityType.LLAMA,
            EntityType.FOX,
            EntityType.CAT,
            EntityType.ARMADILLO,
            EntityType.CAMEL,
            EntityType.BEE,
            EntityType.MOOSHROOM,
            EntityType.MULE,
            EntityType.NAUTILUS,
            EntityType.WOLF,
            EntityType.TURTLE,
            EntityType.BAT,
            EntityType.GLOW_SQUID,
            EntityType.SQUID,
            EntityType.SALMON,
            EntityType.COD,
            EntityType.PUFFERFISH,
            EntityType.TROPICAL_FISH,
            EntityType.SPIDER,
            EntityType.HORSE

    );

    public ElfDamageBlockerListener(RaceManager raceManager) {
        this.raceManager = raceManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        RaceType race = raceManager.getRace(player);
        if (race != RaceType.ELF) return;

        if (blockedAnimals.contains(event.getEntityType())) {
            event.setCancelled(true);
            player.sendMessage("§cNie możesz atakować zwierząt jako Elf!");
        }
    }
}
