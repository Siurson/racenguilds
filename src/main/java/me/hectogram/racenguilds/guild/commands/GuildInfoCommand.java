package me.hectogram.racenguilds.guild.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.hectogram.racenguilds.guild.GuildRole;
import me.hectogram.racenguilds.guild.GuildRoleManager;
import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.stream.Collectors;

public class GuildInfoCommand implements BasicCommand {

    private final RaceManager raceManager;
    private final GuildRoleManager roleManager;

    public GuildInfoCommand(RaceManager raceManager, GuildRoleManager roleManager) {
        this.raceManager = raceManager;
        this.roleManager = roleManager;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        Player player = (Player) source.getSender();
        RaceType race = raceManager.getRace(player);

        if (race == null) {
            player.sendMessage("§cNie posiadasz rasy.");
            return;
        }

        player.sendMessage("§6--- Informacje o Twojej gildii (" + race.getDisplayName() + ") ---");

        OfflinePlayer[] allPlayers = Bukkit.getOfflinePlayers();

        // Lista członków w tej rasie
        String members = java.util.Arrays.stream(allPlayers)
                .filter(p -> {
                    RaceType r = raceManager.getRaceByUUID(p.getUniqueId());
                    return r != null && r == race;
                })
                .map(p -> {
                    String name = p.getName() != null ? p.getName() : p.getUniqueId().toString();
                    GuildRole role = roleManager.getRole(Bukkit.getPlayer(p.getUniqueId()));
                    String suffix = "";
                    if (role != null) {
                        suffix = switch (role) {
                            case KING -> " §6[Król]";
                            case DIPLOMAT -> " §b[Dyplomata]";
                        };
                    }
                    return name + suffix;
                })
                .collect(java.util.stream.Collectors.joining(", "));

        long count = java.util.Arrays.stream(allPlayers)
                .filter(p -> {
                    RaceType r = raceManager.getRaceByUUID(p.getUniqueId());
                    return r != null && r == race;
                })
                .count();

        player.sendMessage("§eLiczba członków: §f" + count);
        player.sendMessage("§eCzłonkowie: §f" + members);
    }

}
