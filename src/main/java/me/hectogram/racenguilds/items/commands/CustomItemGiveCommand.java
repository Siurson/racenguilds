package me.hectogram.racenguilds.items.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.hectogram.racenguilds.items.CustomItem;
import me.hectogram.racenguilds.items.CustomItemManager;
import me.hectogram.racenguilds.items.CustomItemType;
import org.bukkit.entity.Player;

public class CustomItemGiveCommand implements BasicCommand {

    private final CustomItemManager itemManager;

    public CustomItemGiveCommand(CustomItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        if (!(source.getSender() instanceof Player player)) return;


        if (!player.isOp()) {
            player.sendMessage("§cTylko operator może używać tej komendy!");
            return;
        }

        if (args.length != 1) {
            player.sendMessage("§cUżycie: /customitemgive <id>");
            return;
        }

        CustomItemType type = CustomItemType.fromId(args[0]);
        if (type == null) {
            player.sendMessage("§cNieznany item!");
            return;
        }

        CustomItem item = itemManager.get(type);
        if (item == null) {
            player.sendMessage("§cItem nie jest zarejestrowany!");
            return;
        }

        player.getInventory().addItem(item.createItem());
        player.sendMessage("§aOtrzymano item: §e" + type.getId());
    }
}
