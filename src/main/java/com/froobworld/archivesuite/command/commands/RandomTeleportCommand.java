package com.froobworld.archivesuite.command.commands;

import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.command.NabCommand;
import com.froobworld.archivesuite.teleport.random.RandomTeleportSpot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RandomTeleportCommand extends NabCommand {
    private final ArchiveSuite archiveSuite;

    public RandomTeleportCommand(ArchiveSuite archiveSuite) {
        super(
                "randomteleport",
                "Teleport to a random spot on the map.",
                "archivesuite.command.randomteleport",
                Player.class,
                "rtp", "tpr", "wild"
        );
        this.archiveSuite = archiveSuite;
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        Player player = (Player) context.getSender();

        RandomTeleportSpot randomTeleportSpot = archiveSuite.getRandomTeleportManager().getMapRandomTeleportManager(archiveSuite.getMapManager().getMap(player)).getRandomTeleportSpot();
        if (randomTeleportSpot == null) {
            player.sendMessage(
                    Component.text("There are no random teleport spots on this map.", NamedTextColor.RED)
            );
            return;
        }

        player.sendMessage(Component.text("Preparing to teleport...", NamedTextColor.YELLOW));
        archiveSuite.getPlayerTeleporter().teleportAsync(player, randomTeleportSpot.getLocation()).thenAccept(v -> {
            player.sendMessage(
                    Component.text("Whoosh!").color(NamedTextColor.YELLOW)
            );
        });
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder;
    }
}
