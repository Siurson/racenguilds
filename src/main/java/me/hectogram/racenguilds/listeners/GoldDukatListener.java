package me.hectogram.racenguilds.listeners;

import me.hectogram.racenguilds.economy.GoldDukatyItem;
import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class GoldDukatListener implements Listener {

    private final RaceManager raceManager;
    private final GoldDukatyItem gold;

    public GoldDukatListener(RaceManager raceManager, GoldDukatyItem gold) {
        this.raceManager = raceManager;
        this.gold = gold;
    }


    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Item item = event.getItem();
        ItemStack stack = item.getItemStack();

        if (!gold.isGoldDukat(stack)) return;

        if (raceManager.getRace(player) != RaceType.TRADER) {
            event.setCancelled(true);
            player.sendMessage("§cTylko Traderzy mogą posiadać Złote Dukaty!");
        }
    }




    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (raceManager.getRace(player) == RaceType.TRADER) return;

        boolean removed = false;

        for (ItemStack item : player.getInventory().getContents()) {
            if (gold.isGoldDukat(item)) {
                player.getInventory().remove(item);
                removed = true;
            }
        }

        if (removed) {
            player.sendMessage("§cZłote Dukaty zostały usunięte (tylko dla Traderów).");
        }
    }
}
