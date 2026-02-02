package me.hectogram.racenguilds.listeners;

import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TraderCraftingBlockListener implements Listener {

    private final RaceManager raceManager;

    public TraderCraftingBlockListener(RaceManager raceManager) {
        this.raceManager = raceManager;
    }


    @EventHandler
    public void onUseCraftingTable(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;

        if (event.getClickedBlock().getType() != Material.CRAFTING_TABLE) return;

        Player player = event.getPlayer();

        if (raceManager.getRace(player) == RaceType.TRADER) {
            event.setCancelled(true);
            player.sendMessage("§cTraderzy nie potrafią korzystać ze stołu rzemieślniczego!");
        }
    }
}
