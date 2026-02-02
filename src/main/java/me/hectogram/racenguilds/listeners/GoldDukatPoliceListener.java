package me.hectogram.racenguilds.listeners;

import me.hectogram.racenguilds.economy.GoldDukatyItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class GoldDukatPoliceListener implements Listener {

    private final JavaPlugin plugin;
    private final GoldDukatyItem zloto;

    public GoldDukatPoliceListener(JavaPlugin plugin, GoldDukatyItem zloto) {
        this.plugin = plugin;
        this.zloto = zloto;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        purgeGold(player);
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;


        Bukkit.getScheduler().runTaskLater(plugin, () -> purgeGold(player), 1L);
    }


    private void purgeGold(Player player) {
        boolean removed = false;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && zloto.isGoldDukat(item)) {
                player.getInventory().remove(item);

                player.getWorld().dropItemNaturally(player.getLocation(), item);
                removed = true;
            }
        }

        if (removed) {
            player.sendMessage("§cZłote Dukaty zostały usunięte (tylko Traderzy mogą je posiadać).");
        }
    }
}
