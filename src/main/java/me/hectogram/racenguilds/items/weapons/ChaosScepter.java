package me.hectogram.racenguilds.items.weapons;

import me.hectogram.racenguilds.items.CustomItem;
import me.hectogram.racenguilds.items.CustomItemType;
import me.hectogram.racenguilds.items.util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class ChaosScepter implements CustomItem {

    private static final List<PotionEffectType> EFFECT_POOL = List.of(
            PotionEffectType.SLOWNESS,
            PotionEffectType.POISON,
            PotionEffectType.WEAKNESS,
            PotionEffectType.ABSORPTION,
            PotionEffectType.FIRE_RESISTANCE,
            PotionEffectType.SLOW_FALLING,
            PotionEffectType.MINING_FATIGUE,
            PotionEffectType.DARKNESS,
            PotionEffectType.INVISIBILITY,
            PotionEffectType.GLOWING,
            PotionEffectType.SATURATION,
            PotionEffectType.WITHER,
            PotionEffectType.BLINDNESS,
            PotionEffectType.LEVITATION
    );

    private static final Random random = new Random();

    @Override
    public CustomItemType getType() {
        return CustomItemType.CHAOS_SCEPTER;
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(new ItemStack(Material.CARROT_ON_A_STICK))
                .name("§5Berło Chaosu")
                .id(getType().getId())
                .model(NamespacedKey.fromString("item:chaos_scepter", Bukkit.getPluginManager().getPlugin("Racenguilds")))
                .lore("§7PPM aby rzucić chaotyczne zaklęcie")
                .build();
    }

    @Override
    public void onRightClick(Player player) {

        Location eye = player.getEyeLocation();
        Vector direction = eye.getDirection();
        double range = 50.0;

        RayTraceResult result = player.getWorld().rayTrace(
                eye,
                direction,
                range,
                FluidCollisionMode.NEVER,
                true,
                0.5,
                entity -> entity != player
        );

        Location spawnLoc;

        if (result != null) {
            if (result.getHitBlock() != null) {
                spawnLoc = result.getHitBlock().getLocation().add(0.5, 1.2, 0.5);
            } else if (result.getHitEntity() != null) {
                spawnLoc = result.getHitEntity().getLocation().add(0, 1.0, 0);
            } else {
                spawnLoc = eye.clone().add(direction.multiply(range));
            }
        } else {
            spawnLoc = eye.clone().add(direction.multiply(range));
        }

        ThrownPotion potion = player.getWorld().spawn(spawnLoc, ThrownPotion.class);
        potion.setItem(createChaosPotion());
        potion.setShooter(player);
    }

    private ItemStack createChaosPotion() {
        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        meta.setDisplayName("§5Chaos");

        int effectsCount = 2 + random.nextInt(2); // 2–3 efekty

        for (int i = 0; i < effectsCount; i++) {
            PotionEffectType type = EFFECT_POOL.get(random.nextInt(EFFECT_POOL.size()));
            meta.addCustomEffect(
                    new PotionEffect(type, 300, 2),
                    true
            );
        }

        potion.setItemMeta(meta);
        return potion;
    }

    @Override
    public boolean hasCooldown() {
        return true;
    }

    @Override
    public int getCooldownSeconds() {
        return 600;
    }
}
