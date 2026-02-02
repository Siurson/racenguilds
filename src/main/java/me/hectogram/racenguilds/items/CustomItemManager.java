package me.hectogram.racenguilds.items;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CustomItemManager {

    private final Map<CustomItemType, CustomItem> items = new HashMap<>();

    public void register(CustomItem item) {
        items.put(item.getType(), item);
    }

    public CustomItem get(CustomItemType type) {
        return items.get(type);
    }

    public CustomItem fromItem(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) return null;

        var meta = stack.getItemMeta();
        if (!meta.getPersistentDataContainer().has(ItemKeys.ITEM_ID)) return null;

        String id = meta.getPersistentDataContainer()
                .get(ItemKeys.ITEM_ID, org.bukkit.persistence.PersistentDataType.STRING);

        CustomItemType type = CustomItemType.fromId(id);
        return type == null ? null : items.get(type);
    }
}
