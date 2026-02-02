package me.hectogram.racenguilds.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class TradeContractItem {

    private final JavaPlugin plugin;

    public TradeContractItem(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack create() {
        ItemStack item = new ItemStack(Material.FLOW_BANNER_PATTERN);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§eKontrakt Handlowy");
        meta.setLore(Arrays.asList("§7Zapewnia pasywny dochód",
                "§7gdy trzymany w ręce"));

        meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "trade_contract"),
                PersistentDataType.BYTE,
                (byte) 1
        );

        item.setItemMeta(meta);
        return item;
    }

    public boolean isContract(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(new NamespacedKey(plugin, "trade_contract"), PersistentDataType.BYTE);
    }
}
