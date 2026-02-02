package me.hectogram.racenguilds.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.hectogram.racenguilds.items.util.WarActManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class AktaCommand implements BasicCommand {

    private final WarActManager warActManager;
    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public AktaCommand(WarActManager warActManager) {
        this.warActManager = warActManager;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender sender = source.getSender();

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cTylko gracz może użyć tej komendy.");
            return;
        }

        if (!player.isOp()) {
            player.sendMessage("§cBrak uprawnień.");
            return;
        }

        if (args.length == 0) {
            sendUsage(player);
            return;
        }

        switch (args[0].toLowerCase()) {


            case "list" -> {
                Set<String> acts = warActManager.getAllActUUIDs();

                if (acts.isEmpty()) {
                    player.sendMessage("§7Brak akt wojny.");
                    return;
                }

                player.sendMessage("§6§lAkty wojny:");
                for (String uuid : acts) {
                    String name = warActManager.getPlayerName(uuid);
                    long time = warActManager.getTimestamp(uuid);
                    String date = dateFormat.format(new Date(time));

                    player.sendMessage("§e- §f" + name +
                            " §7(" + uuid + ")" +
                            " §8[" + date + "]");
                }
            }


            case "read" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUżycie: /akta read <uuid>");
                    return;
                }

                String uuid = args[1];
                List<String> pages = warActManager.getPages(uuid);

                if (pages.isEmpty()) {
                    player.sendMessage("§cNie znaleziono aktu.");
                    return;
                }

                String name = warActManager.getPlayerName(uuid);
                player.sendMessage("§4§lAkt wojny — §7" + name);

                int i = 1;
                for (String page : pages) {
                    player.sendMessage("§8--- Strona " + i++ + " ---");
                    player.sendMessage("§7" + page);
                }
            }


            case "remove" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUżycie: /akta remove <uuid>");
                    return;
                }

                boolean removed = warActManager.removeAct(args[1]);
                if (removed) {
                    player.sendMessage("§aAkt został usunięty.");
                } else {
                    player.sendMessage("§cNie znaleziono aktu.");
                }
            }


            case "clear" -> {
                warActManager.clearAll();
                player.sendMessage("§c§lWszystkie akty wojny zostały usunięte.");
            }

            default -> sendUsage(player);
        }
    }

    private void sendUsage(Player player) {
        player.sendMessage("§e/akta list");
        player.sendMessage("§e/akta read <uuid>");
        player.sendMessage("§e/akta remove <uuid>");
        player.sendMessage("§e/akta clear");
    }
}
