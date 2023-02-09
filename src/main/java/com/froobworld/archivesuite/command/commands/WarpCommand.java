package com.froobworld.archivesuite.command.commands;

import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.command.NabCommand;
import com.froobworld.archivesuite.command.argument.arguments.WarpArgument;
import com.froobworld.archivesuite.teleport.warp.Warp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand extends NabCommand {
    private final ArchiveSuite archiveSuite;

    public WarpCommand(ArchiveSuite archiveSuite) {
        super(
                "warp",
                "Teleport to a warp.",
                "archivesuite.command.warp",
                Player.class
        );
        this.archiveSuite = archiveSuite;
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        Player player = (Player) context.getSender();
        Warp warp = context.get("warp");
        player.sendMessage(Component.text("Preparing to teleport...", NamedTextColor.YELLOW));
        archiveSuite.getPlayerTeleporter().teleportAsync(player, warp.getLocation()).thenAccept(v -> {
            player.sendMessage(
                    Component.text("Whoosh!").color(NamedTextColor.YELLOW)
            );
        });
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder
                .argument(new WarpArgument<>(
                        true,
                        "warp",
                        archiveSuite.getWarpManager(),
                        archiveSuite.getMapManager(),
                        context -> (Player) context.getSender()
                ));
    }
}
