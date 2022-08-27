package com.froobworld.archivesuite.command.commands;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.DoubleArgument;
import cloud.commandframework.context.CommandContext;
import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.command.NabCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCoordsCommand extends NabCommand {
    private final ArchiveSuite archiveSuite;

    public TeleportCoordsCommand(ArchiveSuite archiveSuite) {
        super(
                "tpcoords",
                "Teleport to coordinates in your current world.",
                "archivesuite.command.tpcoords",
                Player.class,
                "tpc", "teleportcoords", "tpcoord"
        );
        this.archiveSuite = archiveSuite;
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        Player player = (Player) context.getSender();
        Location location = new Location(player.getWorld(), context.get("x"), context.get("y"), context.get("z"));
        player.sendMessage(Component.text("Preparing to teleport...", NamedTextColor.YELLOW));
        archiveSuite.getPlayerTeleporter().teleportAsync(player, location)
                .thenRun(() -> player.sendMessage(Component.text("Whoosh!", NamedTextColor.YELLOW)));
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder
                .argument(DoubleArgument.<CommandSender>newBuilder("x").withMin((int) -30e6).withMax((int) 30e6))
                .argument(DoubleArgument.<CommandSender>newBuilder("y").withMin(-64).withMax(320))
                .argument(DoubleArgument.<CommandSender>newBuilder("z").withMin((int) -30e6).withMax((int) 30e6));
    }
}
