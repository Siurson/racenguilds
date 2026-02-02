package me.hectogram.racenguilds.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class CaravanHornItem {

    private final JavaPlugin plugin;

    public CaravanHornItem(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack create() {
        ItemStack item = new ItemStack(Material.GOAT_HORN);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§6Róg Karawany");
        meta.setLore(Arrays.asList("§7Wzywa 3 wędrownych handlarzy w pobliżu",
                "§cCooldown: 30 minut"));


        meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "caravan_horn"),
                PersistentDataType.BYTE,
                (byte) 1
        );

        item.setItemMeta(meta);
        return item;
    }

    public boolean isCaravanHorn(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(new NamespacedKey(plugin, "caravan_horn"), PersistentDataType.BYTE);
    }
}
