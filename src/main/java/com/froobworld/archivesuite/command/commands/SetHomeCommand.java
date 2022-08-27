package com.froobworld.archivesuite.command.commands;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.command.NabCommand;
import com.froobworld.archivesuite.command.argument.arguments.StringArgument;
import com.froobworld.archivesuite.command.argument.predicate.ArgumentPredicate;
import com.froobworld.archivesuite.command.argument.predicate.predicates.PatternArgumentPredicate;
import com.froobworld.archivesuite.teleport.home.Home;
import com.froobworld.archivesuite.teleport.home.HomeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand extends NabCommand {
    private final ArchiveSuite archiveSuite;

    public SetHomeCommand(ArchiveSuite archiveSuite) {
        super(
                "sethome",
                "Set a home at your location.",
                "archivesuite.command.sethome",
                Player.class
        );
        this.archiveSuite = archiveSuite;
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        Player player = (Player) context.getSender();
        String homeName = context.get("name");
        String map = archiveSuite.getMapManager().getMap(player);
        if (map == null) {
            player.sendMessage(Component.text("You can't set a home in this world.", NamedTextColor.RED));
            return;
        }

        Home home = archiveSuite.getHomeManager().getMapHomeManager(map).createHome(player, homeName);
        player.sendMessage(
                Component.text("Created home '" + home.getName() + "' at your location.").color(NamedTextColor.YELLOW)
        );
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder
                .argument(
                        new StringArgument<>(
                                false,
                                "name",
                                "default",
                                false,
                                new PatternArgumentPredicate<>(
                                        HomeManager.homeNamePattern,
                                        "Name must only contain letters, numbers, underscores and dashes"
                                ),
                                new ArgumentPredicate<>(
                                        true,
                                        (context, string) -> {
                                            String map = archiveSuite.getMapManager().getMap((Player) context.getSender());
                                            if (map == null) {
                                                return true;
                                            }
                                            return archiveSuite.getHomeManager().getMapHomeManager(map).getHomes((Player) context.getSender()).getHome(string) == null;
                                        },
                                        "Home already exists"
                                )
                        ),
                        ArgumentDescription.of("name")
                );
    }
}
