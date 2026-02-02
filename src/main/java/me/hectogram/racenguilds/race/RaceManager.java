package me.hectogram.racenguilds.race;

import me.hectogram.racenguilds.data.DataManager;
import me.hectogram.racenguilds.economy.DukatyItem;
import me.hectogram.racenguilds.guild.GuildRoleManager;
import me.hectogram.racenguilds.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RaceManager {

    private static final Map<UUID, RaceType> playerRaces = new HashMap<>();

    private final JavaPlugin plugin;
    private final DataManager dataManager;
    private TeamManager teamManager;
    private final RaceEffectsManager effectsManager;
    private final EmblemFactory emblemFactory;
    private final DukatyItem dukatyItem;
    private final GuildRoleManager guildRoleManager;

    public RaceManager(JavaPlugin plugin,
                       TeamManager teamManager,
                       RaceEffectsManager effectsManager,
                       DukatyItem dukatyItem,
                       GuildRoleManager guildRoleManager) {
        this.plugin = plugin;
        this.emblemFactory = new EmblemFactory(plugin);
        this.dataManager = new DataManager(plugin);
        this.teamManager = teamManager;
        this.effectsManager = effectsManager;
        this.dukatyItem = dukatyItem;
        this.guildRoleManager = guildRoleManager;
    }

    public void loadAllOnlinePlayers() {
        Bukkit.getOnlinePlayers().forEach(this::loadRace);
    }

    public RaceType getRaceByUUID(UUID uuid) {
        return playerRaces.get(uuid);
    }

    public void setRace(Player player, RaceType race) {
        effectsManager.beginRaceSetup(player);

        playerRaces.put(player.getUniqueId(), race);
        dataManager.saveRace(player.getUniqueId(), race);

        effectsManager.applyPerks(player, race);
        teamManager.apply(player, race);

        ItemStack emblem = emblemFactory.createEmblem(race);
        player.getInventory().addItem(emblem);
        player.sendMessage("§aOtrzymałeś emblemat swojej rasy.");


        if (race == RaceType.TRADER) {
            player.getInventory().addItem(
                    new ItemStack(Material.STONE_SWORD),
                    new ItemStack(Material.STONE_PICKAXE),
                    new ItemStack(Material.STONE_AXE)
            );
            player.sendMessage("§aOtrzymałeś kamienne narzędzia!");
            player.getInventory().addItem(dukatyItem.create(30));
            player.sendMessage("§bOtrzymałeś 30 srebrnych dukatów!");
        } else {
            int amount = switch (race) {
                case HUMAN -> 7;
                case ELF -> 6;
                case GOBLIN -> 8;
                case ORC -> 2;
                case DWARF -> 5;
                case GIANT -> 3;
                default -> 5;
            };
            player.getInventory().addItem(dukatyItem.create(amount));
            player.sendMessage("§bOtrzymałeś " + amount + " srebrnych dukatów!");
        }


        Bukkit.getScheduler().runTaskLater(plugin,
                () -> effectsManager.endRaceSetup(player), 2L);
    }

    public void resetRace(Player player) {
        UUID uuid = player.getUniqueId();
        playerRaces.remove(uuid);
        dataManager.deleteRace(uuid);

        effectsManager.resetPlayer(player);
        teamManager.remove(player);
    }

    public RaceType getRace(UUID uuid) {
        return playerRaces.get(uuid);
    }

    public RaceType getRace(Player player) {
        return playerRaces.get(player.getUniqueId());
    }

    public boolean hasRace(Player player) {
        return playerRaces.containsKey(player.getUniqueId());
    }

    public RaceType loadRace(Player player) {
        RaceType race = dataManager.loadRace(player.getUniqueId());
        if (race != null) {
            playerRaces.put(player.getUniqueId(), race);

            effectsManager.applyPerks(player, race);
            teamManager.apply(player, race);

            guildRoleManager.applyRole(player);
        }
        return race;
    }





    public void setTeamManager(TeamManager teamManager) {
        this.teamManager = teamManager;
    }
}
