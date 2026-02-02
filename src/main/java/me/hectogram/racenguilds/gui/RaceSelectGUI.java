package me.hectogram.racenguilds.gui;

import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.hectogram.racenguilds.Racenguilds;

import java.util.List;

public class RaceSelectGUI implements Listener {

    private static final String TITLE = "§6Wybierz rasę";
    private final RaceManager raceManager;

    public RaceSelectGUI(RaceManager raceManager) {
        this.raceManager = raceManager;
    }

    public void open(Player player) {

        if (raceManager.hasRace(player)) {
            player.sendMessage("§cMasz już wybraną rasę!");
            return;
        }
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        inv.setItem(10, createItem(Material.PLAYER_HEAD, "§fCzłowiek", java.util.List.of(
                "§fNeutralna",
                "§7Standardowa rasa z nielicznymi bonusami"
        )));
        inv.setItem(11, createItem(Material.IRON_PICKAXE, "§7Krasnolud", java.util.List.of(
                "§fNeutralna",
                "§7Większość życia przeżyli w podziemiach,",
                "§7dlatego mimo małych rozmiarów są całkiem wytrwali",
                "§7i mają doświadczenie w kopalniach"

        )));
        inv.setItem(12, createItem(Material.IRON_AXE, "§cOrk", java.util.List.of(
                "§cAgresywna",
                "§7Ich naturalna budowa jest stworzona do walki,",
                "§7lecz ich czaszka uniemożliwia założenie hełmu"
        )));
        inv.setItem(13, createItem(Material.EMERALD, "§aGoblin", java.util.List.of(
                "§fNeutralna",
                "§7Szybkie małe i niezwykle zręczne stworki,",
                "§7ich wadą jest niezawielka odporność i atak"
        )));
        inv.setItem(14, createItem(Material.OAK_SAPLING, "§2Elf", java.util.List.of(
                "§fNeutralna",
                "§7Eksperci w dziedzinie łucznictwa, posiadają sowi wzrok,",
                "§7ale są wegetarianami i nie odważą się skrzywdzić zwierzat"
        )));
        inv.setItem(15, createItem(Material.STONE, "§8Gigant", java.util.List.of(
                "§cAgresywna",
                "§7Ich wielka masa i siła, powoduje że są niezniszczalnym pogromem,",
                "§7dopóki przyjdzie czas założyć zbroje w której się nie mieszczą..."
        )));
        inv.setItem(16, createItem(Material.CHEST, "§ePodróżny Kupiec", java.util.List.of(
                "§aPacyfistyczna",
                "§7Gildia Kupiecka złożona dawnych magów, którzy odpuścili wojny i skupili się na handlu.",
                "§7Nie dysponują zdolnościami ale dysponują niezłym sprzętem.",
                "§8Nie posiadają hierarchi"
        )));

        player.openInventory(inv);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!event.getView().getTitle().equals(TITLE)) return;
        Player player = (Player) event.getPlayer();
        if (!raceManager.hasRace(player)) {
            Bukkit.getScheduler().runTaskLater(org.bukkit.plugin.java.JavaPlugin.getPlugin(org.bukkit.plugin.java.JavaPlugin.class), () -> open(player), 1L);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().getTitle().equals(TITLE)) return;

        event.setCancelled(true);


        if (raceManager.hasRace(player)) {
            player.sendMessage("§cMasz już wybraną rasę!");
            player.closeInventory();
            return;
        }

        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

        String name = event.getCurrentItem().getItemMeta().getDisplayName();
        RaceType race = switch (name) {
            case "§fCzłowiek" -> RaceType.HUMAN;
            case "§7Krasnolud" -> RaceType.DWARF;
            case "§cOrk" -> RaceType.ORC;
            case "§aGoblin" -> RaceType.GOBLIN;
            case "§2Elf" -> RaceType.ELF;
            case "§8Gigant" -> RaceType.GIANT;
            case "§ePodróżny Kupiec" -> RaceType.TRADER;
            default -> null;
        };

        if (race == null) return;

        raceManager.setRace(player, race);
        player.closeInventory();
        player.sendMessage("§aWybrałeś rasę: §e" + race.name());
    }

    private ItemStack createItem(Material mat, String name, List<String> loreLines) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(loreLines);
            item.setItemMeta(meta);
        }
        return item;
    }
}
