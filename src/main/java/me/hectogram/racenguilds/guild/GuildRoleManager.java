package me.hectogram.racenguilds.guild;

import me.hectogram.racenguilds.data.DataManager;
import me.hectogram.racenguilds.data.RoleDataManager;
import me.hectogram.racenguilds.race.RaceManager;
import me.hectogram.racenguilds.race.RaceType;
import me.hectogram.racenguilds.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuildRoleManager {

    private final Map<UUID, GuildRole> roles = new HashMap<>();
    private final RoleDataManager roleDataManager;
    private RaceManager raceManager;
    private TeamManager teamManager;
    private DataManager dataManager;

    public GuildRoleManager(RoleDataManager roleDataManager, RaceManager raceManager, DataManager dataManager) {
        this.roleDataManager = roleDataManager;
        this.raceManager = raceManager;
        this.teamManager = teamManager;
        this.dataManager = dataManager;
    }

    public GuildRole getRole(Player player) {
        return roles.get(player.getUniqueId());
    }

    public RaceManager getRaceManager() { return raceManager; }


    public void applyRole(Player player) {
        GuildRole role = roles.get(player.getUniqueId());
        if (role == null) return;

        RaceType race = raceManager.getRace(player);
        if (race == null) return;


        teamManager.apply(player, race);

        teamManager.applyRoleColor(player, role);
    }

    public boolean assignRole(Player target, GuildRole role) {
        RaceType race = raceManager.getRace(target);
        if (race == null || race == RaceType.TRADER) return false;

        roles.put(target.getUniqueId(), role);
        roleDataManager.saveRole(target.getUniqueId(), role);

        applyRole(target);
        return true;
    }

    public boolean removeRole(Player player) {
        roles.remove(player.getUniqueId());
        roleDataManager.deleteRole(player.getUniqueId());

        applyRole(player); // refresh
        return true;
    }

    // yaml
    public void loadRole(Player player) {
        GuildRole role = roleDataManager.loadRole(player.getUniqueId());
        if (role != null) {
            roles.put(player.getUniqueId(), role);
            applyRole(player);
        }
    }


    public void reloadRolesForOnlinePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            loadRole(player);
            GuildRole role = getRole(player);


            RaceType race = raceManager.getRace(player);


            if (role == null || race == null) continue;


            teamManager.apply(player, race);


            teamManager.applyRoleColor(player, role);
        }

    }




    public void setRaceManager(RaceManager raceManager) {
        this.raceManager = raceManager;
    }

    public void setTeamManager(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

}

