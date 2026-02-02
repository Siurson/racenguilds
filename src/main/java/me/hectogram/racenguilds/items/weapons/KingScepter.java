package me.hectogram.racenguilds.items.weapons;

import me.hectogram.racenguilds.guild.GuildRole;
import me.hectogram.racenguilds.guild.GuildRoleManager;
import me.hectogram.racenguilds.items.CustomItem;
import me.hectogram.racenguilds.items.CustomItemType;
import me.hectogram.racenguilds.items.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class KingScepter implements CustomItem {

    private final GuildRoleManager roleManager;
    private final JavaPlugin plugin; // <- dodajemy tu

    public KingScepter(GuildRoleManager roleManager, JavaPlugin plugin) {
        this.roleManager = roleManager;
        this.plugin = plugin;
    }

    @Override
    public CustomItemType getType() {
        return CustomItemType.KING_SCEPTER;
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(new ItemStack(Material.CARROT_ON_A_STICK))
                .name("§6Berło Króla")
                .id(getType().getId())
                .model(NamespacedKey.fromString("item:king_scepter", Bukkit.getPluginManager().getPlugin("Racenguilds")))
                .lore("§7PPM aby przyzwać pioruny (tylko król)")
                .build();
    }

    @Override
    public void onRightClick(Player player) {
        if (roleManager == null) {
            player.sendMessage("§cBłąd: brak GuildRoleManager!");
            return;
        }

        GuildRole role = roleManager.getRole(player);

        if (role != GuildRole.KING) {
            player.sendMessage("§cTylko król może używać tego berła!");
            return;
        }

        Location target = player.getTargetBlockExact(100).getLocation().add(0.5, 1, 0.5);
        int strikes = 12; // liczba piorunów
        long delay = 10L; // opóźnienie między piorunami w tickach (1 tick = 0.05s)
        for (int i = 0; i < strikes; i++) {
            final int index = i;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.getWorld().strikeLightning(target);
                if (index == strikes - 1) {
                    player.sendMessage("§aPrzyzwałeś pioruny!");
                }
            }, i * delay);
        }
    }

    @Override
    public boolean hasCooldown() {
        return true;
    }

    @Override
    public int getCooldownSeconds() {
        return 3600; // dla debugu
    }
}
