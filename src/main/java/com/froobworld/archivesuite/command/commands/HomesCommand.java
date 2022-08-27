package com.froobworld.archivesuite.command.commands;

import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.command.NabCommand;
import com.froobworld.archivesuite.teleport.home.Home;
import com.froobworld.archivesuite.teleport.home.Homes;
import com.froobworld.archivesuite.util.NumberDisplayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HomesCommand extends NabCommand {
    private final ArchiveSuite archiveSuite;

    public HomesCommand(ArchiveSuite archiveSuite) {
        super(
                "homes",
                "Get a list of homes you have in a given map.",
                "archivesuite.command.homes",
                Player.class
        );
        this.archiveSuite = archiveSuite;
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        Player player = (Player) context.getSender();
        String map = archiveSuite.getMapManager().getMap(player);
        Homes homes = archiveSuite.getHomeManager().getMapHomeManager(map) == null ? null : archiveSuite.getHomeManager().getMapHomeManager(map).getHomes(player);
        if (homes == null || homes.getHomes().isEmpty()) {
            player.sendMessage(Component.text("You have no homes in this map.", NamedTextColor.YELLOW));
        } else {
            List<TextComponent> homeList = homes.getHomes().stream()
                    .map(Home::getName)
                    .sorted()
                    .map(Component::text)
                    .toList();
            player.sendMessage(Component.text("You have " + NumberDisplayer.toStringWithModifier(homeList.size(), " home", " homes", false) + " in this map.", NamedTextColor.YELLOW));
            player.sendMessage(Component.join(JoinConfiguration.separator(Component.text(", ")), homeList));
        }
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder;
    }
}
