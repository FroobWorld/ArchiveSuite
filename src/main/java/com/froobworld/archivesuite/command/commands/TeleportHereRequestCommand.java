package com.froobworld.archivesuite.command.commands;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.parsers.PlayerArgument;
import cloud.commandframework.context.CommandContext;
import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.command.NabCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportHereRequestCommand extends NabCommand {
    private final ArchiveSuite archiveSuite;

    public TeleportHereRequestCommand(ArchiveSuite archiveSuite) {
        super(
                "tpahere",
                "Request a player to teleport to your location.",
                "archivesuite.command.tpahere",
                Player.class
        );
        this.archiveSuite = archiveSuite;
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        Player sender = (Player) context.getSender();
        Player subject =  context.get("player");
        archiveSuite.getTeleportRequestHandler().requestSummon(sender, subject);
        sender.sendMessage(
                Component.text("Teleport request sent.").color(NamedTextColor.YELLOW)
        );

        subject.sendMessage(
                sender.displayName().append(
                        Component.text(" has requested for you to teleport to them.")
                ).color(NamedTextColor.YELLOW)
        );
        subject.sendMessage(
                Component.text("Use ").append(
                        Component.text("/tpaccept").color(NamedTextColor.GREEN).clickEvent(ClickEvent.runCommand("/tpaccept"))
                ).append(
                        Component.text(" to accept.")
                ).color(NamedTextColor.YELLOW)
        );
        subject.sendMessage(
                Component.text("Use ").append(
                        Component.text("/tpdeny").color(NamedTextColor.RED).clickEvent(ClickEvent.runCommand("/tpdeny"))
                ).append(
                        Component.text(" to decline.")
                ).color(NamedTextColor.YELLOW)
        );
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder
                .argument(PlayerArgument.newBuilder("player"));
    }
}
