package com.froobworld.archivesuite.command.commands;

import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.command.NabCommand;
import com.froobworld.archivesuite.teleport.spawn.MapSpawnManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends NabCommand {
    private final ArchiveSuite archiveSuite;

    public SpawnCommand(ArchiveSuite archiveSuite) {
        super(
                "spawn",
                "Teleport to a map's spawn.",
                "archivesuite.command.spawn",
                Player.class
        );
        this.archiveSuite = archiveSuite;
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        Player player = (Player) context.getSender();
        MapSpawnManager mapSpawnManager = archiveSuite.getSpawnManager().getMapSpawnManager(archiveSuite.getMapManager().getMap(player));

        player.sendMessage(Component.text("Preparing to teleport...", NamedTextColor.YELLOW));
        archiveSuite.getPlayerTeleporter().teleportAsync(player, mapSpawnManager.getSpawnLocation()).thenAccept(v -> {
            player.sendMessage(
                    Component.text("Teleported to spawn.").color(NamedTextColor.YELLOW)
            );
        });
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder;
    }
}
