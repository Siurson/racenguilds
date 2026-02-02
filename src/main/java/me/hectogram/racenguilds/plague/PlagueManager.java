package me.hectogram.racenguilds.plague;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlagueManager {

    private final Set<UUID> infected = new HashSet<>();

    public void infect(Player player) {
        infected.add(player.getUniqueId());
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.HUNGER,
                Integer.MAX_VALUE,
                0,
                false,
                false
        ));
    }

    public void cure(Player player) {
        infected.remove(player.getUniqueId());
        player.removePotionEffect(PotionEffectType.HUNGER);
    }

    public void applyPlagueEffects(Player player) {

        player.addPotionEffect(new PotionEffect(
                PotionEffectType.HUNGER,
                Integer.MAX_VALUE,
                0,
                false,
                false
        ));
    }

    public boolean isInfected(Player player) {
        return infected.contains(player.getUniqueId());
    }

    public void clearAll() {
        infected.clear();
    }
}
