package me.hectogram.racenguilds.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.hectogram.racenguilds.gui.TraderShopGUI;
import org.bukkit.entity.Player;

public class ShopCommand implements BasicCommand {

    private final TraderShopGUI traderShopGUI;

    public ShopCommand(TraderShopGUI traderShopGUI) {
        this.traderShopGUI = traderShopGUI;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        if (!(source.getSender() instanceof Player player)) return;

        traderShopGUI.open(player);
    }
}
