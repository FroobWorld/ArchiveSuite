package com.froobworld.archivesuite.command.commands;

import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.command.NabCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackCommand extends NabCommand {
    private final ArchiveSuite archiveSuite;

    public BackCommand(ArchiveSuite archiveSuite) {
        super(
                "back",
                "Teleport to your previous location.",
                "archivesuite.command.back",
                Player.class
        );
        this.archiveSuite = archiveSuite;
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        Player player = (Player) context.getSender();
        Location backLocation = archiveSuite.getBackManager().getBackLocation(player);
        if (backLocation == null) {
            player.sendMessage(
                    Component.text("There is nowhere to go back to.").color(NamedTextColor.RED)
            );
            return;
        }
        player.sendMessage(Component.text("Preparing to teleport...", NamedTextColor.YELLOW));
        archiveSuite.getPlayerTeleporter().teleportAsync(player, backLocation).thenAccept(location -> {
            player.sendMessage(
                    Component.text("Teleported to your previous location.").color(NamedTextColor.YELLOW)
            );
        });
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder;
    }
}
