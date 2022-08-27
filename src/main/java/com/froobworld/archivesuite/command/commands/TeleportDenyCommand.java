package com.froobworld.archivesuite.command.commands;

import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.command.NabCommand;
import com.froobworld.archivesuite.teleport.request.TeleportRequestHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportDenyCommand extends NabCommand {
    private final ArchiveSuite archiveSuite;

    public TeleportDenyCommand(ArchiveSuite archiveSuite) {
        super(
                "tpdeny",
                "Deny another player's teleport request.",
                "archivesuite.command.tpdeny",
                Player.class
        );
        this.archiveSuite = archiveSuite;
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        Player sender = (Player) context.getSender();
        TeleportRequestHandler.RequestedTeleport requestedTeleport = archiveSuite.getTeleportRequestHandler().getRequestedTeleport(sender);
        if (requestedTeleport == null || !requestedTeleport.isValid()) {
            sender.sendMessage(
                    Component.text("You have no teleport requests to decline.").color(NamedTextColor.RED)
            );
            return;
        }
        requestedTeleport.invalidate();
        sender.sendMessage(
                Component.text("Teleport request declined.").color(NamedTextColor.YELLOW)
        );
        requestedTeleport.getRequester().sendMessage(
                sender.displayName().append(
                        Component.text(" declined your teleport request.")
                ).color(NamedTextColor.YELLOW)
        );
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder;
    }
}
