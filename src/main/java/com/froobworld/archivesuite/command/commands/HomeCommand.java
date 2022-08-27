package com.froobworld.archivesuite.command.commands;

import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.command.NabCommand;
import com.froobworld.archivesuite.command.argument.arguments.HomeArgument;
import com.froobworld.archivesuite.teleport.home.Home;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand extends NabCommand {
    private final ArchiveSuite archiveSuite;

    public HomeCommand(ArchiveSuite archiveSuite) {
        super(
                "home",
                "Teleport to your home.",
                "archivesuite.command.home",
                Player.class
        );
        this.archiveSuite = archiveSuite;
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        Player player = (Player) context.getSender();
        Home home = context.get("home");
        player.sendMessage(Component.text("Preparing to teleport...", NamedTextColor.YELLOW));
        archiveSuite.getPlayerTeleporter().teleportAsync(player, home.getLocation()).thenAccept(v -> {
            player.sendMessage(
                    Component.text("There's no place like home...").color(NamedTextColor.YELLOW)
            );
        });
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder
                .argument(new HomeArgument<>(
                        false,
                        "home",
                        archiveSuite.getHomeManager(),
                        archiveSuite.getMapManager(),
                        context -> (Player) context.getSender()
                ));
    }
}
