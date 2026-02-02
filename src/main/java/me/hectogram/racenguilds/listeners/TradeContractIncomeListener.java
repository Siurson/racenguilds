package me.hectogram.racenguilds.listeners;

import me.hectogram.racenguilds.economy.DukatyItem;
import me.hectogram.racenguilds.items.TradeContractItem;
import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class TradeContractIncomeListener implements Listener {

    private final JavaPlugin plugin;
    private final RaceManager raceManager;
    private final DukatyItem srebro;
    private final TradeContractItem contract;


    private final int INCOME_AMOUNT = 1;


    private final long INTERVAL_TICKS = 3000L;


    private final Set<Player> warnedPlayers = new HashSet<>();

    public TradeContractIncomeListener(JavaPlugin plugin, RaceManager raceManager, DukatyItem srebro, TradeContractItem contract) {
        this.plugin = plugin;
        this.raceManager = raceManager;
        this.srebro = srebro;
        this.contract = contract;

        startIncomeTask();
    }

    private void startIncomeTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ItemStack main = player.getInventory().getItemInMainHand();
                ItemStack off = player.getInventory().getItemInOffHand();

                boolean hasContract = contract.isContract(main) || contract.isContract(off);

                if (hasContract) {
                    if (raceManager.getRace(player) == RaceType.TRADER) {
                        player.getInventory().addItem(srebro.create(INCOME_AMOUNT));

                        warnedPlayers.remove(player); // reset warning
                    } else {

                        if (!warnedPlayers.contains(player)) {
                            player.sendMessage("§cTylko Traderzy otrzymują dochód z kontraktu!");
                            warnedPlayers.add(player);
                        }
                    }
                } else {

                    warnedPlayers.remove(player);
                }
            }
        }, 0L, INTERVAL_TICKS);
    }
}
