package me.hectogram.racenguilds.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.hectogram.racenguilds.economy.DukatyItem;
import me.hectogram.racenguilds.guild.GuildRole;
import me.hectogram.racenguilds.guild.GuildRoleManager;
import me.hectogram.racenguilds.items.util.createWarActBook;
import me.hectogram.racenguilds.race.RaceManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AktWojnyCommand implements BasicCommand {

    private final JavaPlugin plugin;
    private final RaceManager raceManager;
    private final DukatyItem dukatyItem;
    private final GuildRoleManager guildRoleManager;

    private static final int COST = 64;

    public AktWojnyCommand(JavaPlugin plugin, RaceManager raceManager, GuildRoleManager guildRoleManager) {
        this.plugin = plugin;
        this.raceManager = raceManager;
        this.dukatyItem = new DukatyItem(plugin);
        this.guildRoleManager = guildRoleManager;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        Player player = (Player) source.getSender();


        GuildRole role = guildRoleManager.getRole(player);
        if (role != GuildRole.DIPLOMAT) {
            player.sendMessage("§cTylko dyplomata może użyć tej komendy!");
            return;
        }


        ItemStack inHand = player.getInventory().getItemInMainHand();
        if (!dukatyItem.isDukat(inHand) || inHand.getAmount() < 64) {
            player.sendMessage("§cPotrzebujesz 64 srebrnych dukatów aby wygenerować akt wojny!");
            return;
        }



        inHand.setAmount(inHand.getAmount() - 64);
        player.getInventory().setItemInMainHand(inHand);


        ItemStack akt = createWarActBook.createWarActBook(plugin, player);
        player.getInventory().addItem(akt);

        player.sendMessage("§aOtrzymałeś akt wojny!");
    }




    private boolean hasEnoughDukats(Player player, int amount) {
        int count = 0;

        for (ItemStack item : player.getInventory().getContents()) {
            if (dukatyItem.isDukat(item)) {
                count += item.getAmount();
            }
        }
        return count >= amount;
    }

    private void removeDukats(Player player, int amount) {
        int remaining = amount;

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);

            if (!dukatyItem.isDukat(item)) continue;

            int stackAmount = item.getAmount();

            if (stackAmount <= remaining) {
                remaining -= stackAmount;
                player.getInventory().setItem(i, null);
            } else {
                item.setAmount(stackAmount - remaining);
                remaining = 0;
            }

            if (remaining <= 0) break;
        }
    }
}
