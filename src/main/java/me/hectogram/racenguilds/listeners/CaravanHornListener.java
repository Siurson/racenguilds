package me.hectogram.racenguilds.listeners;

import me.hectogram.racenguilds.items.CaravanHornItem;
import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CaravanHornListener implements Listener {

    private final RaceManager raceManager;
    private final CaravanHornItem horn;
    private final JavaPlugin plugin;


    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final long COOLDOWN = 30 * 60 * 20L;

    public CaravanHornListener(JavaPlugin plugin, RaceManager raceManager, CaravanHornItem horn) {
        this.plugin = plugin;
        this.raceManager = raceManager;
        this.horn = horn;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (!(event.getHand() == EquipmentSlot.HAND)) return;
        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!horn.isCaravanHorn(item)) return;

        if (raceManager.getRace(player) != RaceType.TRADER) {
            player.sendMessage("§cTylko Trader może używać Róg Karawany!");
            return;
        }

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();
        if (cooldowns.containsKey(uuid) && now - cooldowns.get(uuid) < 30 * 60 * 1000L) {
            long remaining = (30 * 60 * 1000L - (now - cooldowns.get(uuid))) / 1000L;
            player.sendMessage("§cRóg Karawany jest na cooldownie! Pozostało: " + remaining + " sekund");
            return;
        }


        Location loc = player.getLocation();
        for (int i = 0; i < 3; i++) {
            WanderingTrader trader = player.getWorld().spawn(loc.add(Math.random() * 5, 0, Math.random() * 5), WanderingTrader.class);
            trader.setInvulnerable(true);
        }

        player.sendMessage("§aWezwano 3 wędrownych handlarzy!");
        cooldowns.put(uuid, now);
    }
}
