package me.hectogram.racenguilds.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class BlessedFlourCommand implements BasicCommand {

    private final RaceManager raceManager;

    public BlessedFlourCommand(RaceManager raceManager) {
        this.raceManager = raceManager;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        if (!(source.getSender() instanceof Player player)) {
            source.getSender().sendMessage("§cTylko gracze mogą używać tej komendy!");
            return;
        }


        if (raceManager.getRace(player) != RaceType.HUMAN) {
            player.sendMessage("§cTylko ludzie mogą użyć błogosławionej mąki!");
            return;
        }


        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.getType() != Material.WHEAT || hand.getAmount() == 0) {
            player.sendMessage("§cMusisz trzymać pszenicę w ręce!");
            return;
        }


        int amount = hand.getAmount();
        hand.setAmount(0);
        player.getInventory().setItemInMainHand(hand);

        ItemStack blessedFlour = new ItemStack(Material.SUGAR, amount);
        ItemMeta meta = blessedFlour.getItemMeta();
        meta.setDisplayName("§6Błogosławiona Mąka");
        meta.setLore(Collections.singletonList("§7Można wymienić na błogosławiony chleb"));
        blessedFlour.setItemMeta(meta);

        player.getInventory().addItem(blessedFlour);
        player.sendMessage("§aTwoja pszenica została zamieniona na Błogosławioną Mąkę!");
    }
}
