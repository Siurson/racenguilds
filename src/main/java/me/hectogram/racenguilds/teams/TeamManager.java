package me.hectogram.racenguilds.teams;

import me.hectogram.racenguilds.guild.GuildRole;
import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamManager {

    private final Scoreboard scoreboard;
    private final RaceManager raceManager;

    public TeamManager(RaceManager raceManager) {
        this.raceManager = raceManager;
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        registerTeams();
    }


    private void registerTeams() {
        for (RaceType race : RaceType.values()) {

            Team team = scoreboard.getTeam(race.name());
            if (team == null) {
                team = scoreboard.registerNewTeam(race.name());
            }

            team.setPrefix("§7[" + race.getDisplayName() + "] ");
        }
    }


    public void remove(Player player) {
        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }
    }



    public void apply(Player player, RaceType race) {

        Team current = scoreboard.getEntryTeam(player.getName());
        if (current != null && current.getName().equals(race.name())) {
            return;
        }

        if (current != null) {
            current.removeEntry(player.getName());
        }

        Team team = scoreboard.getTeam(race.name());
        if (team != null) {
            team.addEntry(player.getName());
        }

        player.setScoreboard(scoreboard);
    }


    public void applyRoleColor(Player player, GuildRole role) {

        RaceType race = raceManager.getRace(player);
        if (race == null) return;

        Team team = scoreboard.getTeam(race.name());
        if (team == null) return;

        if (!team.hasEntry(player.getName())) {
            team.addEntry(player.getName());
        }

        if (role == null) {
            team.setSuffix("");
            return;
        }

        switch (role) {
            case KING -> team.setSuffix(" §6[Król]");
            case DIPLOMAT -> team.setSuffix(" §b[Dyplomata]");
        }
    }

}
