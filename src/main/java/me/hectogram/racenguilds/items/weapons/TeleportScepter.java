package me.hectogram.racenguilds.items.weapons;

import me.hectogram.racenguilds.items.CustomItem;
import me.hectogram.racenguilds.items.CustomItemType;
import me.hectogram.racenguilds.items.util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class TeleportScepter implements CustomItem {

    private final int range = 100; // maksymalny zasięg teleportu

    @Override
    public CustomItemType getType() {
        return CustomItemType.TELEPORT_SCEPTER;
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(new ItemStack(Material.CARROT_ON_A_STICK))
                .name("§dBerło Teleportacji")
                .id(getType().getId())
                .model(NamespacedKey.fromString("item:teleport_scepter", Bukkit.getPluginManager().getPlugin("Racenguilds")))
                .lore("§7PPM aby teleportować się tam, gdzie patrzysz")
                .build();
    }

    @Override
    public void onRightClick(Player player) {

        Location eye = player.getEyeLocation();
        Vector direction = eye.getDirection();

        // RayTrace w kierunku, max 30 bloków
        RayTraceResult result = player.getWorld().rayTrace(
                eye,
                direction,
                range,
                FluidCollisionMode.NEVER,
                true,
                0.5,
                entity -> false // nie interesuje nas trafienie w moby
        );

        Location target;
        if (result != null) {
            if (result.getHitBlock() != null) {
                target = result.getHitBlock().getLocation().add(0.5, 1, 0.5); // lekko nad blok
            } else {
                target = eye.clone().add(direction.multiply(range));
            }
        } else {
            target = eye.clone().add(direction.multiply(range));
        }

        // Teleportacja
        player.teleport(target);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 50, 0.5, 1, 0.5, 0.1);
    }

    @Override
    public boolean hasCooldown() {
        return true;
    }

    @Override
    public int getCooldownSeconds() {
        return 100; // debugowo 10 sekund
    }
}
