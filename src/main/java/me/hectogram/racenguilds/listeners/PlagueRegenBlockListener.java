package me.hectogram.racenguilds.listeners;

import me.hectogram.racenguilds.plague.PlagueManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlagueRegenBlockListener implements Listener {

    private final PlagueManager plagueManager;

    public PlagueRegenBlockListener(PlagueManager plagueManager) {
        this.plagueManager = plagueManager;
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED
                && plagueManager.isInfected(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();


        if (plagueManager.isInfected(player)) {

            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.HUNGER,
                    Integer.MAX_VALUE,
                    0,
                    false,
                    false
            ));


        }
    }

}
