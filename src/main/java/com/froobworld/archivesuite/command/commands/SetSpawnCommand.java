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

public class SetSpawnCommand extends NabCommand {
    private final ArchiveSuite archiveSuite;

    public SetSpawnCommand(ArchiveSuite archiveSuite) {
        super(
                "setspawn",
                "Set a map's spawn location.",
                "archivesuite.command.setspawn",
                Player.class
        );
        this.archiveSuite = archiveSuite;
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        Player player = (Player) context.getSender();
        MapSpawnManager mapSpawnManager = archiveSuite.getSpawnManager().getMapSpawnManager(archiveSuite.getMapManager().getMap(player));

        mapSpawnManager.setSpawnLocation(player.getLocation());
        player.sendMessage(Component.text("Spawn location set.", NamedTextColor.YELLOW));
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder;
    }
}
