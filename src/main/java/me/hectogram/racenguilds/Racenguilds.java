package me.hectogram.racenguilds;

import me.hectogram.racenguilds.commands.*;
import me.hectogram.racenguilds.data.DataManager;
import me.hectogram.racenguilds.data.RoleDataManager;
import me.hectogram.racenguilds.economy.DukatyItem;
import me.hectogram.racenguilds.economy.GoldDukatyItem;
import me.hectogram.racenguilds.guild.GuildRole;
import me.hectogram.racenguilds.guild.GuildRoleManager;
import me.hectogram.racenguilds.guild.commands.ForceRoleCommand;
import me.hectogram.racenguilds.guild.commands.GuildInfoCommand;
import me.hectogram.racenguilds.guild.commands.GuildsInfoCommand;
import me.hectogram.racenguilds.items.CaravanHornItem;
import me.hectogram.racenguilds.items.CustomItemManager;
import me.hectogram.racenguilds.items.ItemKeys;
import me.hectogram.racenguilds.items.TradeContractItem;
import me.hectogram.racenguilds.gui.RaceSelectGUI;
import me.hectogram.racenguilds.gui.TraderShopGUI;
import me.hectogram.racenguilds.items.commands.CustomItemGiveCommand;
import me.hectogram.racenguilds.items.cooldown.ItemCooldownManager;
import me.hectogram.racenguilds.items.listeners.CustomItemListener;
import me.hectogram.racenguilds.items.util.WarActManager;
import me.hectogram.racenguilds.items.weapons.*;
import me.hectogram.racenguilds.listeners.*;
import me.hectogram.racenguilds.plague.PlagueManager;
import me.hectogram.racenguilds.race.*;
import me.hectogram.racenguilds.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Racenguilds extends JavaPlugin {

    private static Racenguilds instance;

    private RaceManager raceManager;
    private RaceEffectsManager effectsManager;
    private TeamManager teamManager;
    private PlagueManager plagueManager;
    private GuildRoleManager guildRoleManager;


    private DukatyItem srebro;
    private GoldDukatyItem zloto;
    private CaravanHornItem horn;
    private TradeContractItem contract;

    private PvPManager pvpManager;
    private DataManager dataManager;


    @Override
    public void onEnable() {
        instance = this;


        this.dataManager = new DataManager(this);
        RoleDataManager roleDataManager = new RoleDataManager(this);


        this.effectsManager = new RaceEffectsManager(this);


        this.plagueManager = new PlagueManager();


        this.pvpManager = new PvPManager();

        WarActManager warActManager = new WarActManager(this);
        getServer().getPluginManager().registerEvents(
                new WarActBookListener(this, warActManager),
                this
        );





        // ekonoma
        this.srebro = new DukatyItem(this);
        this.zloto = new GoldDukatyItem(this);
        this.horn = new CaravanHornItem(this);
        this.contract = new TradeContractItem(this);


        this.teamManager = null;


        this.guildRoleManager = new GuildRoleManager(roleDataManager, null, dataManager);


        this.raceManager = new RaceManager(this, null, effectsManager, srebro, guildRoleManager);


        this.teamManager = new TeamManager(raceManager);


        guildRoleManager.setRaceManager(raceManager);
        guildRoleManager.setTeamManager(teamManager);
        raceManager.setTeamManager(teamManager);


        this.guildRoleManager = new GuildRoleManager(roleDataManager, null, null); // na start null
        this.guildRoleManager.setRaceManager(raceManager);
        this.guildRoleManager.setTeamManager(teamManager);

        // ity
        CustomItemManager itemManager = new CustomItemManager();
        ItemCooldownManager cooldownManager = new ItemCooldownManager();
        itemManager.register(new SlowScepter());
        itemManager.register(new TeleportScepter());
        itemManager.register(new SightScepter());
        itemManager.register(new FireScepter());
        itemManager.register(new ChaosScepter());
        itemManager.register(new WindScepter());
        itemManager.register(new KingScepter(guildRoleManager, this)); // <-- problem

        // guiras
        RaceSelectGUI gui = new RaceSelectGUI(raceManager);
        getServer().getPluginManager().registerEvents(gui, this);


        getServer().getPluginManager().registerEvents(new CustomItemListener(itemManager, cooldownManager), this);
        new ArmorChecker(this, raceManager, effectsManager);

        getServer().getPluginManager().registerEvents(new VaccineDrinkListener(plagueManager), this);
        getServer().getPluginManager().registerEvents(new PlagueRegenBlockListener(plagueManager), this);


        this.registerCommand("forcerole", new ForceRoleCommand(guildRoleManager, teamManager, raceManager));


        getServer().getPluginManager().registerEvents(
                new PlayerJoinListener(
                        raceManager,
                        effectsManager,
                        gui,
                        plagueManager,
                        guildRoleManager,
                        teamManager,
                        this
                ),
                this
        );

        getServer().getPluginManager().registerEvents(
                new PlayerRespawnListener(raceManager, effectsManager, this, plagueManager),
                this
        );

        // traderghu
        TraderShopGUI traderShopGUI = new TraderShopGUI(raceManager, srebro, zloto, horn, contract);
        getServer().getPluginManager().registerEvents(traderShopGUI, this);


        getServer().getPluginManager().registerEvents(new GoldDukatPoliceListener(this, zloto), this);
        getServer().getPluginManager().registerEvents(new TradeContractIncomeListener(this, raceManager, srebro, contract), this);
        getServer().getPluginManager().registerEvents(new CaravanHornListener(this, raceManager, horn), this);
        getServer().getPluginManager().registerEvents(new PvPListener(pvpManager, raceManager), this);
        getServer().getPluginManager().registerEvents(new GoldDukatListener(raceManager, zloto), this);
        getServer().getPluginManager().registerEvents(new ElfDamageBlockerListener(raceManager), this);
        getServer().getPluginManager().registerEvents(new TraderCraftingBlockListener(raceManager), this);



        ItemKeys.ITEM_ID = new NamespacedKey(this, "custom_item");


        DukatyItem dukatyItem = new DukatyItem(this);
        // komedy
        this.registerCommand("dukaty", new DukatyCommand(srebro));
        this.registerCommand("reset", new ResetRaceCommand(raceManager, effectsManager));
        this.registerCommand("pvp", new PvPCommand(pvpManager));
        this.registerCommand("blessflour", new BlessedFlourCommand(raceManager));
        this.registerCommand("plaga", new PlagueCommand(plagueManager));
        this.registerCommand("akta", new AktaCommand(warActManager));

        this.registerCommand("vaccine", new VaccineCommand(raceManager, plagueManager));
        this.registerCommand("customitemgive", new CustomItemGiveCommand(itemManager));
        this.registerCommand("shop", new ShopCommand(traderShopGUI));
        this.registerCommand("aktwojny",
                new AktWojnyCommand(
                        this,
                        raceManager,
                        guildRoleManager
                )
        );




        this.registerCommand("guildinfo", new GuildInfoCommand(raceManager, guildRoleManager));
        this.registerCommand("guildsinfo", new GuildsInfoCommand(raceManager, guildRoleManager));



        Bukkit.getScheduler().runTaskLater(this, () -> {
            raceManager.loadAllOnlinePlayers();

        }, 5L);


        Bukkit.getScheduler().runTaskLater(this, () -> {
            getLogger().info("Reloading guild roles for online players...");
            guildRoleManager.reloadRolesForOnlinePlayers();
        }, 40L);


        getLogger().info("RaceNGuilds enabled");
    }
}

