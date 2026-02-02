package me.hectogram.racenguilds.commands;

import me.hectogram.racenguilds.plague.PlagueManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class PlagueCommand implements BasicCommand {

    private final PlagueManager plagueManager;

    public PlagueCommand(PlagueManager plagueManager) {
        this.plagueManager = plagueManager;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        if (!(source.getSender() instanceof Player sender)) return;
        if (!sender.isOp()) {
            sender.sendMessage("§cBrak uprawnień.");
            return;
        }

        if (args.length == 0) {
            sender.sendMessage("§cUżycie: /plaga start | clear");
            return;
        }

        switch (args[0].toLowerCase()) {

            case "start" -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    plagueManager.infect(p);
                    p.sendTitle(
                            "§cPLAGA",
                            "§7Zaraza rozprzestrzenia się...",
                            10, 60, 10
                    );
                }
                sender.sendMessage("§aPlaga została rozpoczęta.");
            }

            case "clear" -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    plagueManager.cure(p);
                }
                plagueManager.clearAll();
                Bukkit.broadcastMessage("§aPlaga została całkowicie usunięta.");
            }

            default -> sender.sendMessage("§cUżycie: /plaga start | clear");
        }
    }
}
