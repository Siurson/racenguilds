package me.hectogram.racenguilds.items.weapons;

import me.hectogram.racenguilds.items.CustomItem;

import me.hectogram.racenguilds.items.CustomItem;
import me.hectogram.racenguilds.items.CustomItemType;
import me.hectogram.racenguilds.items.util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class SightScepter implements CustomItem {

    @Override
    public CustomItemType getType() {
        return CustomItemType.SIGHT_SCEPTER;
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(new ItemStack(Material.CARROT_ON_A_STICK))
                .name("§eBerło Widzenia")
                .id(getType().getId())
                .model(NamespacedKey.fromString("item:sight_scepter", Bukkit.getPluginManager().getPlugin("Racenguilds")))
                .lore("§7PPM aby zobaczyć wszystkich w okolicy")
                .build();
    }

    @Override
    public void onRightClick(Player player) {
        for (Player p : player.getWorld().getPlayers()) {
            if (p.equals(player)) continue;
            if (p.getLocation().distance(player.getLocation()) <= 100) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 0));
            }
        }
    }

    @Override
    public boolean hasCooldown() { return true; }

    @Override
    public int getCooldownSeconds() { return 300; }
}
