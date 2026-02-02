package me.hectogram.racenguilds.listeners;

import me.hectogram.racenguilds.Racenguilds;
import me.hectogram.racenguilds.gui.RaceSelectGUI;
import me.hectogram.racenguilds.guild.GuildRole;
import me.hectogram.racenguilds.guild.GuildRoleManager;
import me.hectogram.racenguilds.race.RaceEffectsManager;
import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import me.hectogram.racenguilds.plague.PlagueManager;
import me.hectogram.racenguilds.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final RaceManager raceManager;
    private final RaceEffectsManager effectsManager;
    private final RaceSelectGUI raceSelectGUI;
    private final PlagueManager plagueManager;
    private final GuildRoleManager guildRoleManager;
    private final TeamManager teamManager;
    private final Racenguilds plugin;

    public PlayerJoinListener(RaceManager raceManager,
                              RaceEffectsManager effectsManager,
                              RaceSelectGUI gui,
                              PlagueManager plagueManager,
                              GuildRoleManager guildRoleManager,
                              TeamManager teamManager,
                              Racenguilds plugin) {
        this.raceManager = raceManager;
        this.effectsManager = effectsManager;
        this.raceSelectGUI = gui;
        this.plagueManager = plagueManager;
        this.guildRoleManager = guildRoleManager;
        this.teamManager = teamManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();


        Bukkit.getScheduler().runTaskLater(plugin, () -> {

            raceManager.loadRace(player);

            if (!raceManager.hasRace(player)) {
                raceSelectGUI.open(player);
                return;
            }

            RaceType race = raceManager.getRace(player);

            effectsManager.applyPerks(player, race);
            teamManager.apply(player, race);


            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                guildRoleManager.loadRole(player);
                GuildRole role = guildRoleManager.getRole(player);

                if (role != null) {

                    teamManager.applyRoleColor(player, role);
                } else {

                    teamManager.applyRoleColor(player, null);
                }
            }, 40L);


            if (plagueManager.isInfected(player)) {
                plagueManager.applyPlagueEffects(player);
            }

        }, 2L);
    }
}
