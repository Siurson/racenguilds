package me.hectogram.racenguilds.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import me.hectogram.racenguilds.economy.DukatyItem;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class DukatyCommand implements BasicCommand {

    private final DukatyItem dukatyItem;

    public DukatyCommand(DukatyItem dukatyItem) {
        this.dukatyItem = dukatyItem;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender sender = source.getSender();
        if (!(sender instanceof Player player)) return;

        if (!player.isOp()) {
            player.sendMessage("§cTylko operatorzy mogą używać tej komendy!");
            return;
        }

        int amount = 64;
        if (args.length == 1) {
            try {
                amount = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        player.getInventory().addItem(dukatyItem.create(amount));
        player.sendMessage("§aOtrzymałeś §b" + amount + " §adukatów!");
    }
}
