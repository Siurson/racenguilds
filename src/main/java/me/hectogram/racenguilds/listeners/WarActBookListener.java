package me.hectogram.racenguilds.listeners;

import me.hectogram.racenguilds.items.util.WarActManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import java.util.List;

public class WarActBookListener implements Listener {

    private final JavaPlugin plugin;
    private final WarActManager warActManager;

    public WarActBookListener(JavaPlugin plugin, WarActManager warActManager) {
        this.plugin = plugin;
        this.warActManager = warActManager;


    }

    public static void removeWarActBook(Player player, JavaPlugin plugin) {
        PlayerInventory inv = player.getInventory();
        NamespacedKey key = new NamespacedKey(plugin, "war_act");

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null) continue;
            if (!(item.getItemMeta() instanceof BookMeta meta)) continue;

            if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                inv.setItem(i, null);
                break;
            }
        }
    }

    @EventHandler
    public void onBookSign(PlayerEditBookEvent event) {
        if (!event.isSigning()) return;

        Player player = event.getPlayer();
        BookMeta meta = event.getNewBookMeta();

        NamespacedKey key = new NamespacedKey(plugin, "war_act");
        if (!meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            return;
        }


        List<String> pages = meta.getPages();

        warActManager.saveAct(
                player.getUniqueId(),
                player.getName(),
                pages
        );


        Bukkit.getScheduler().runTask(plugin, () -> {
            removeWarActBook(player, plugin);
        });

        player.sendMessage("§aAkt został złożony.");
    }

}
