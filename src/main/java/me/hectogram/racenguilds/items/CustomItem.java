package me.hectogram.racenguilds.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface CustomItem {

    CustomItemType getType();

    ItemStack createItem();

    default void onRightClick(Player player) {}

    default boolean hasCooldown() {
        return false;
    }

    default int getCooldownSeconds() {
        return 0;
    }
}
