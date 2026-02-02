package me.hectogram.racenguilds.gui;

import me.hectogram.racenguilds.economy.DukatyItem;
import me.hectogram.racenguilds.economy.GoldDukatyItem;
import me.hectogram.racenguilds.items.CaravanHornItem;
import me.hectogram.racenguilds.items.TradeContractItem;
import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TraderShopGUI implements Listener {

    private static final String TITLE = "§6Sklep Traderów";

    private static final int GOLD_PRICE = 10;
    private static final int HORN_PRICE = 40;
    private static final int CONTRACT_PRICE = 120;

    private final DukatyItem srebro;
    private final GoldDukatyItem zloto;
    private final CaravanHornItem horn;
    private final TradeContractItem contract;
    private final RaceManager raceManager;

    public TraderShopGUI(RaceManager raceManager,
                         DukatyItem srebro,
                         GoldDukatyItem zloto,
                         CaravanHornItem horn,
                         TradeContractItem contract) {
        this.raceManager = raceManager;
        this.srebro = srebro;
        this.zloto = zloto;
        this.horn = horn;
        this.contract = contract;
    }

    public void open(Player player) {
        if (raceManager.getRace(player) != RaceType.TRADER) {
            player.sendMessage("§cTylko traderzy mogą korzystać z tego sklepu!");
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 9, TITLE);

        inv.setItem(0, zloto.create(1));        // 10 srebrnych → 1 złoty
        inv.setItem(1, horn.create());          // 40 srebrnych
        inv.setItem(2, contract.create());      // 120 srebrnych
        inv.setItem(3, createExchangeItem());   // 1 srebrny → 3 emeraldy
        inv.setItem(8, createPriceList());      // 🧾 Cennik

        player.openInventory(inv);
    }



    private ItemStack createExchangeItem() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aWymiana Dukatów");
        meta.setLore(List.of(
                "§73 srebrne dukaty",
                "§f→ §a1 emerald",
                "",
                "§8Kliknij aby wymienić"
        ));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createPriceList() {
        ItemStack item = new ItemStack(Material.WHITE_WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§eCennik");
        meta.setLore(List.of(
                "§fZłoty Dukat §7– §e10 §fsrebrnych",
                "§fRóg Karawany §7– §e40 §fsrebrnych",
                "§fKontrakt §7– §e120 §fsrebrnych"
        ));
        item.setItemMeta(meta);
        return item;
    }



    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(TITLE)) return;
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (raceManager.getRace(player) != RaceType.TRADER) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) return;


        if (zloto.isGoldDukat(clicked)) {
            buy(player, GOLD_PRICE, zloto.create(1), "1 złoty dukat");
            return;
        }


        if (horn.isCaravanHorn(clicked)) {
            buy(player, HORN_PRICE, horn.create(), "Róg Karawany");
            return;
        }


        if (contract.isContract(clicked)) {
            buy(player, CONTRACT_PRICE, contract.create(), "Kontrakt Handlowy");
            return;
        }


        if (clicked.getType() == Material.EMERALD &&
                clicked.getItemMeta() != null &&
                clicked.getItemMeta().getDisplayName().contains("Wymiana")) {

            if (!hasEnoughSrebro(player, 3)) {
                player.sendMessage("§cNie masz srebrnych dukatów!");
                return;
            }

            removeSrebro(player, 3);
            player.getInventory().addItem(new ItemStack(Material.EMERALD, 1));
            player.sendMessage("§aWymieniłeś 1 srebrny dukat na 3 emeraldy!");
        }
    }



    private void buy(Player player, int price, ItemStack reward, String name) {
        if (!hasEnoughSrebro(player, price)) {
            player.sendMessage("§cPotrzebujesz §e" + price + " §csrebrnych dukatów!");
            return;
        }

        removeSrebro(player, price);
        player.getInventory().addItem(reward);
        player.sendMessage("§aZakupiono §e" + name + " §aza §e" + price + " §asrebrnych!");
    }

    private boolean hasEnoughSrebro(Player player, int amount) {
        return countSrebro(player) >= amount;
    }

    private int countSrebro(Player player) {
        int total = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (srebro.isDukat(item)) {
                total += item.getAmount();
            }
        }
        return total;
    }

    private void removeSrebro(Player player, int amount) {
        int remaining = amount;
        ItemStack[] contents = player.getInventory().getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item == null) continue;
            if (!srebro.isDukat(item)) continue;

            int stack = item.getAmount();
            if (stack > remaining) {
                item.setAmount(stack - remaining);
                break;
            } else {
                remaining -= stack;
                contents[i] = null;
                if (remaining <= 0) break;
            }
        }

        player.getInventory().setContents(contents);
    }
}
