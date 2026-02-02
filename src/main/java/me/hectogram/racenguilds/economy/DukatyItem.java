package me.hectogram.racenguilds.economy;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class DukatyItem {

    private final JavaPlugin plugin;

    public DukatyItem(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    public ItemStack create(int amount) {
        ItemStack item = new ItemStack(Material.IRON_NUGGET, amount);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§bSrebrny Dukat");
        meta.setLore(Arrays.asList("§7podstawa ekonomi"));


        NamespacedKey modelKey = NamespacedKey.fromString("item:dukat", plugin);
        meta.setItemModel(modelKey);


        meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "dukaty"),
                PersistentDataType.BYTE,
                (byte) 1
        );

        item.setItemMeta(meta);
        return item;
    }


    public boolean isDukat(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(new NamespacedKey(plugin, "dukaty"), PersistentDataType.BYTE);
    }
}
