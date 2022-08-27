package com.froobworld.archivesuite.command.commands;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.command.NabCommand;
import com.froobworld.archivesuite.command.argument.arguments.PageNumberArgument;
import com.froobworld.archivesuite.teleport.warp.Warp;
import com.froobworld.archivesuite.util.ListPaginator;
import com.froobworld.archivesuite.util.NumberDisplayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class WarpsCommand extends NabCommand {
    private static final int ITEMS_PER_PAGE = 20;
    private final ArchiveSuite archiveSuite;

    public WarpsCommand(ArchiveSuite archiveSuite) {
        super("warps",
                "Display a list of warps.",
                "archiveSuite.command.warps",
                Player.class
        );
        this.archiveSuite = archiveSuite;
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        String map = archiveSuite.getMapManager().getMap((Player) context.getSender());

        List<String> warps = archiveSuite.getWarpManager().getMapWarpManager(map).getWarps().stream()
                .map(Warp::getName)
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());

        if (warps.isEmpty()) {
            context.getSender().sendMessage(Component.text("There are no warps.").color(NamedTextColor.YELLOW));
        } else {
            int pageNumber = context.get("page");
            List<String>[] pages = ListPaginator.paginate(warps, ITEMS_PER_PAGE);
            List<String> page = pages[pageNumber - 1];
            context.getSender().sendMessage(
                    Component.text("There " + NumberDisplayer.toStringWithModifierAndPrefix(warps.size(), " warp", " warps", "is ", "are ") + ". ")
                            .append(Component.text("Showing page " + pageNumber + "/" + pages.length + ".")).color(NamedTextColor.YELLOW)
            );
            context.getSender().sendMessage(Component.text(String.join(", ", page)));
        }
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder
                .argument(
                        new PageNumberArgument<>(
                                false,
                                "page",
                                context -> {
                                    String map = archiveSuite.getMapManager().getMap((Player) context.getSender());
                                    return archiveSuite.getWarpManager().getMapWarpManager(map).getWarps().size();
                                },
                                ITEMS_PER_PAGE
                        ),
                        ArgumentDescription.of("page")
                );
    }

}
