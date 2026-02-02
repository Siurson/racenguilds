package me.hectogram.racenguilds.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.hectogram.racenguilds.plague.PlagueManager;
import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import org.bukkit.potion.PotionType;

import java.util.List;

public class VaccineCommand implements BasicCommand {

    private final RaceManager raceManager;
    private final PlagueManager plagueManager;

    public VaccineCommand(RaceManager raceManager, PlagueManager plagueManager) {
        this.raceManager = raceManager;
        this.plagueManager = plagueManager;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        if (!(source.getSender() instanceof Player player)) return;


        RaceType race = raceManager.getRace(player);
        if (race != RaceType.HUMAN) {
            player.sendMessage("§cTylko ludzie mogą stworzyć szczepionkę!");
            return;
        }


        if (!hasIngredients(player)) {
            player.sendMessage("§cNie masz wszystkich wymaganych składników! Wymagane: honeycomb, Miód, Diament, Złota Marchewka");
            return;
        }


        removeIngredients(player);


        ItemStack potion = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§aSzczepionka przeciw Pladze");
            meta.setLore(List.of(
                    "§7Wypij aby usunąć efekt plagi",
                    "§7Zrobiona przez genialnych ludzkich naukowców"
            ));

            potion.setItemMeta(meta);
        }

        player.getInventory().addItem(potion);
        player.sendMessage("§aStworzyłeś szczepionkę przeciw pladze!");
    }

    private boolean hasIngredients(Player player) {
        return player.getInventory().containsAtLeast(new ItemStack(Material.HONEY_BOTTLE), 1) &&
                player.getInventory().containsAtLeast(new ItemStack(Material.HONEYCOMB), 1) &&
                player.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND), 1) &&
                player.getInventory().containsAtLeast(new ItemStack(Material.GOLDEN_CARROT), 1);
    }

    private void removeIngredients(Player player) {
        removeItem(player, Material.HONEY_BOTTLE, 1);
        removeItem(player, Material.HONEYCOMB, 1);
        removeItem(player, Material.DIAMOND, 1);
        removeItem(player, Material.GOLDEN_CARROT, 1);
    }

    private void removeItem(Player player, Material mat, int amount) {
        ItemStack[] contents = player.getInventory().getContents();
        int remaining = amount;

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item == null) continue;
            if (item.getType() != mat) continue;

            if (item.getAmount() > remaining) {
                item.setAmount(item.getAmount() - remaining);
                break;
            } else {
                remaining -= item.getAmount();
                contents[i] = null;
                if (remaining <= 0) break;
            }
        }

        player.getInventory().setContents(contents);
    }
}
