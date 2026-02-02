package me.hectogram.racenguilds.items.weapons;

import me.hectogram.racenguilds.items.CustomItem;

import me.hectogram.racenguilds.items.CustomItem;
import me.hectogram.racenguilds.items.CustomItemType;
import me.hectogram.racenguilds.items.util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.List;

public class WindScepter implements CustomItem {

    @Override
    public CustomItemType getType() {
        return CustomItemType.WIND_SCEPTER;
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(new ItemStack(Material.CARROT_ON_A_STICK))
                .name("§bBerło Wichru")
                .id(getType().getId())
                .model(NamespacedKey.fromString("item:wind_scepter", Bukkit.getPluginManager().getPlugin("Racenguilds")))
                .lore("§7PPM aby odrzucić graczy")
                .build();
    }

    @Override
    public void onRightClick(Player player) {
        for (Entity e : player.getNearbyEntities(5,5,5)) {
            if (e instanceof Player p && !p.equals(player)) {
                Vector knockback = p.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(2);
                p.setVelocity(knockback.add(new Vector(0,1,0)));
            }
        }
    }

    @Override
    public boolean hasCooldown() { return true; }

    @Override
    public int getCooldownSeconds() { return 300; }
}
