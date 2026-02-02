package me.hectogram.racenguilds.economy;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class GoldDukatyItem {

    private final JavaPlugin plugin;

    public GoldDukatyItem(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack create(int amount) {
        ItemStack item = new ItemStack(Material.GOLD_NUGGET, amount);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§6Złoty Dukat");
        meta.setLore(Arrays.asList("§7Waluta specjalna dla traderów"));

        NamespacedKey modelKey = NamespacedKey.fromString("item:gold_dukat", plugin);
        meta.setItemModel(modelKey);

        meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "gold_dukaty"),
                PersistentDataType.BYTE,
                (byte) 1
        );

        item.setItemMeta(meta);
        return item;
    }

    public boolean isGoldDukat(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(new NamespacedKey(plugin, "gold_dukaty"), PersistentDataType.BYTE);
    }
}
