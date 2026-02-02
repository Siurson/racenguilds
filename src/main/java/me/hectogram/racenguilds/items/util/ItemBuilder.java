package me.hectogram.racenguilds.items.util;

import me.hectogram.racenguilds.items.ItemKeys;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemBuilder name(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder lore(String lore) {
        meta.setLore(List.of(lore));
        return this;
    }

    public ItemBuilder model(NamespacedKey key) {
        meta.setItemModel(key);
        return this;
    }

    public ItemBuilder id(String id) {
        meta.getPersistentDataContainer().set(
                ItemKeys.ITEM_ID,
                PersistentDataType.STRING,
                id
        );
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}
