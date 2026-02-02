package me.hectogram.racenguilds.items.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class createWarActBook {

    public static ItemStack createWarActBook(JavaPlugin plugin, Player player) {
        ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setDisplayName("§4Akt Wojny");
        meta.setTitle("Akt Wojny");
        meta.setAuthor(player.getName());

        meta.addPage(
                "§4§lWZOR AKTU\n" +
                        "§0Rodzaj aktu:\n" +
                        "§8(Wojna / Pokój)\n" +

                        "§0Rasa:§8...\n" +


                        "§0Nick gracza:\n" +
                        "§8" + player.getName() + "\n\n" +

                        "§0Powód:\n" +
                        "§8...\n\n" +

                        "§0Podpis:§8...\n" +
                        "§8PRZEPISZ WZOR NA 2 STRONE"
        );



        NamespacedKey key = new NamespacedKey(plugin, "war_act");
        meta.getPersistentDataContainer().set(
                key,
                PersistentDataType.STRING,
                "pending"
        );

        book.setItemMeta(meta);
        return book;
    }
}
