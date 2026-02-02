package me.hectogram.racenguilds.guild.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.hectogram.racenguilds.guild.GuildRole;
import me.hectogram.racenguilds.guild.GuildRoleManager;
import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.teams.TeamManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceRoleCommand implements BasicCommand {

    private final GuildRoleManager roleManager;
    private final RaceManager raceManager;

    public ForceRoleCommand(GuildRoleManager roleManager, TeamManager teamManager, RaceManager raceManager) {
        this.roleManager = roleManager;
        this.raceManager = raceManager;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender sender = source.getSender();

        if (!sender.isOp()) {
            sender.sendMessage("§cBrak uprawnień.");
            return;
        }

        if (args.length != 2) {
            sender.sendMessage("§cUżycie: /forcerole <gracz> <king|diplomat|none>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cGracz offline.");
            return;
        }

        RaceType race = raceManager.getRace(target);
        if (race == RaceType.TRADER) {
            sender.sendMessage("§cTraderzy nie posiadają hierarchii.");
            return;
        }

        if (args[1].equalsIgnoreCase("none")) {
            roleManager.removeRole(target);
            sender.sendMessage("§aUsunięto rolę gracza.");
            target.sendMessage("§cTwoja rola gildyjna została odebrana.");
            return;
        }

        GuildRole role = GuildRole.fromString(args[1]);
        if (role == null) {
            sender.sendMessage("§cNieznana rola.");
            return;
        }

        boolean ok = roleManager.assignRole(target, role);
        if (!ok) {
            sender.sendMessage("§cTa rola jest już zajęta lub gracz nie ma rasy.");
            return;
        }

        sender.sendMessage("§aNadano rolę §e" + role.getDisplay() + " §agraczowi " + target.getName());
        target.sendMessage("§aOtrzymałeś rolę: " + role.getDisplay());
    }
}
