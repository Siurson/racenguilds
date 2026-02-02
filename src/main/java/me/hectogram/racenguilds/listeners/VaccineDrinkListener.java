package me.hectogram.racenguilds.listeners;

import me.hectogram.racenguilds.plague.PlagueManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class VaccineDrinkListener implements Listener {

    private final PlagueManager plagueManager;

    public VaccineDrinkListener(PlagueManager plagueManager) {
        this.plagueManager = plagueManager;
    }

    @EventHandler
    public void onDrink(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta()) return;

        String name = item.getItemMeta().getDisplayName();
        if ("§aSzczepionka przeciw Pladze".equals(name)) {

            plagueManager.cure(event.getPlayer());
            event.getPlayer().sendMessage("§aUdało Ci się wyleczyć plagę!");
        }
    }
}
