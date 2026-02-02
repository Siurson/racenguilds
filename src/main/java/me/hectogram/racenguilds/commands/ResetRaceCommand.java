package me.hectogram.racenguilds.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.hectogram.racenguilds.gui.RaceSelectGUI;
import me.hectogram.racenguilds.race.RaceEffectsManager;
import me.hectogram.racenguilds.race.RaceManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetRaceCommand implements BasicCommand {

    private final RaceManager raceManager;
    private final RaceEffectsManager effectsManager;

    public ResetRaceCommand(RaceManager raceManager, RaceEffectsManager effectsManager) {
        this.raceManager = raceManager;
        this.effectsManager = effectsManager;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        if (!(source.getSender() instanceof Player player)) return;


        if (!player.isOp()) {
            player.sendMessage("§cTylko operator może używać tej komendy!");
            return;
        }

        if (args.length != 1) {
            player.sendMessage("§cUżycie: /reset <gracz>");
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cGracz nie jest online!");
            return;
        }


        raceManager.resetRace(target);
        effectsManager.resetPlayer(target);


        new RaceSelectGUI(raceManager).open(target);


        player.sendMessage("§aRasa gracza " + target.getName() + " została zresetowana!");
        target.sendMessage("§aTwoja rasa została zresetowana! Wybierz nową rasę.");
    }
}
