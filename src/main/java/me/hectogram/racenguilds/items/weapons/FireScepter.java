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

public class FireScepter implements CustomItem {

    @Override
    public CustomItemType getType() {
        return CustomItemType.FIRE_SCEPTER;
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(new ItemStack(Material.CARROT_ON_A_STICK))
                .name("§cBerło Ognia")
                .id(getType().getId())
                .model(NamespacedKey.fromString("item:fire_scepter", Bukkit.getPluginManager().getPlugin("Racenguilds")))
                .lore("§7PPM aby wystrzelić fireball")
                .build();
    }

    @Override
    public void onRightClick(Player player) {
        Location eye = player.getEyeLocation();
        Vector dir = eye.getDirection();

        Fireball fireball = player.getWorld().spawn(eye.add(dir), Fireball.class);
        fireball.setDirection(dir);
        fireball.setShooter(player);
        fireball.setYield(2F); // wielkość wybuchu
        fireball.setIsIncendiary(true);
    }

    @Override
    public boolean hasCooldown() { return true; }

    @Override
    public int getCooldownSeconds() { return 300; }
}
