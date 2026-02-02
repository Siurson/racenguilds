package me.hectogram.racenguilds.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.hectogram.racenguilds.PvPManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvPCommand implements BasicCommand {

    private final PvPManager pvpManager;

    public PvPCommand(PvPManager manager) {
        this.pvpManager = manager;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender sender = source.getSender();

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cTylko gracze mogą używać tej komendy!");
            return;
        }

        if (!player.isOp()) {
            player.sendMessage("§cNie masz uprawnień do tej komendy!");
            return;
        }

        if (args.length < 1) {
            player.sendMessage("§eUżycie: /pvp <on|off> [wiadomość]");
            return;
        }

        boolean enable;
        String title;
        String subtitle;

        switch (args[0].toLowerCase()) {
            case "on" -> {
                enable = true;
                title = "§4§lSTAN WOJENNY";
                subtitle = args.length > 1
                        ? String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length))
                        : "§cPvP zostało włączone!";
            }
            case "off" -> {
                enable = false;
                title = "§2§lSTAN POKOJU";
                subtitle = args.length > 1
                        ? String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length))
                        : "§aPvP zostało wyłączone!";
            }
            default -> {
                player.sendMessage("§eUżycie: /pvp <on|off> [wiadomość]");
                return;
            }
        }

        pvpManager.setPvPEnabled(enable);

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.sendTitle(
                    title,
                    "§f" + subtitle,
                    10,
                    70,
                    20
            );
        }

        player.sendMessage("§aZmieniono stan PvP.");
    }
}
