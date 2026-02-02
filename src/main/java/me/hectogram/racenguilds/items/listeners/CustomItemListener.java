package me.hectogram.racenguilds.items.listeners;

import me.hectogram.racenguilds.items.CustomItem;
import me.hectogram.racenguilds.items.CustomItemManager;
import me.hectogram.racenguilds.items.CustomItemType;
import me.hectogram.racenguilds.items.cooldown.ItemCooldownManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CustomItemListener implements Listener {

    private final CustomItemManager itemManager;
    private final ItemCooldownManager cooldownManager;

    public CustomItemListener(CustomItemManager itemManager,
                              ItemCooldownManager cooldownManager) {
        this.itemManager = itemManager;
        this.cooldownManager = cooldownManager;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;

        Player player = event.getPlayer();

        CustomItem item = itemManager.fromItem(
                player.getInventory().getItemInMainHand()
        );

        if (item == null) return;

        event.setCancelled(true);

        CustomItemType type = item.getType();

        if (item.hasCooldown() && cooldownManager.hasCooldown(player, type)) {
            long left = cooldownManager.getRemaining(player, type);
            player.sendMessage("§cCooldown: §e" + left + "s");
            return;
        }

        item.onRightClick(player);

        if (item.hasCooldown()) {
            cooldownManager.setCooldown(player, type, item.getCooldownSeconds());
        }
    }
}
