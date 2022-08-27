package com.froobworld.archivesuite.command.commands;

import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.command.NabCommand;
import com.froobworld.archivesuite.teleport.home.Homes;
import com.froobworld.archivesuite.teleport.home.MapHomeManager;
import com.froobworld.archivesuite.teleport.warp.MapWarpManager;
import com.froobworld.archivesuite.util.NumberDisplayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MapsCommand extends NabCommand {
    private final ArchiveSuite archiveSuite;

    public MapsCommand(ArchiveSuite archiveSuite) {
        super(
                "maps",
                "Get a list of available maps.",
                "archivesuite.command.maps",
                Player.class
        );
        this.archiveSuite = archiveSuite;
    }


    @Override
    public void execute(CommandContext<CommandSender> context) {
        List<String> maps = archiveSuite.getMapManager().getEnabledMaps().stream()
                .sorted((string1, string2) -> -string1.compareToIgnoreCase(string2))
                .toList();
        context.getSender().sendMessage(
                Component.text("There are " + maps.size() + " available maps.", NamedTextColor.YELLOW)
        );

        for (String map : maps) {
            Component homeCountText;
            MapHomeManager homeManager = archiveSuite.getHomeManager().getMapHomeManager(map);
            Homes homes = homeManager == null ? null : homeManager.getHomes((Player) context.getSender());
            int homeCount = homes == null ? 0 : homes.getHomes().size();
            if (homeCount == 0) {
                homeCountText = Component.text("You have no homes available on this map.");
            } else {
                homeCountText = Component.text("You have " + NumberDisplayer.toStringWithModifier(homeCount, " home", " homes", false) + " available on this map.");
            }

            Component warpCountText;
            MapWarpManager warpManager = archiveSuite.getWarpManager().getMapWarpManager(map);
            int warpCount = warpManager == null ? 0 : warpManager.getWarps().size();
            if (warpCount == 0) {
                warpCountText = Component.text("There are no warps available on this map.");
            } else {
                warpCountText = Component.text("There " + NumberDisplayer.toStringWithModifierAndPrefix(warpCount, " warp", " warps", "is ", "are ") + " available on this map.");
            }

            List<String> blurbStrings = archiveSuite.getArchiveSuiteConfig().mapBlurb.of(map.trim()).get();

            Component infoText = Component.empty();
            for (String string : blurbStrings) {
                Component toAppend = MiniMessage.miniMessage().deserialize(
                        string,
                        TagResolver.resolver("home_count", Tag.inserting(homeCountText)),
                        TagResolver.resolver("warp_count", Tag.inserting(warpCountText))
                );
                if (infoText == Component.empty()) {
                    infoText = toAppend;
                } else {
                    infoText = Component.join(JoinConfiguration.separator(Component.newline()), infoText, toAppend);
                }
            }

            Component teleportComponent = Component.text("[", NamedTextColor.GRAY)
                    .append(Component.text("Click to teleport", NamedTextColor.GRAY).decorate(TextDecoration.ITALIC))
                    .append(Component.text("]", NamedTextColor.GRAY))
                    .clickEvent(ClickEvent.runCommand("/map " + map))
                    .hoverEvent(Component.text("Click to teleport to " + map + "."));

            Component information = Component.text("[", NamedTextColor.GRAY)
                    .append(Component.text("Hover for info", NamedTextColor.GRAY).decorate(TextDecoration.ITALIC))
                    .append(Component.text("]", NamedTextColor.GRAY))
                    .hoverEvent(HoverEvent.showText(infoText));

            context.getSender().sendMessage(
                    Component.text("- ")
                            .append(Component.text(map.equals("lobby") ? "lobby " : map, NamedTextColor.GOLD)) // hack, looks nicer...
                            .append(Component.space())
                            .append(Component.space())
                            .append(Component.space())
                            .append(information)
                            .append(Component.space())
                            .append(Component.space())
                            .append(Component.space())
                            .append(Component.space())
                            .append(teleportComponent)
            );

        }
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder;
    }
}
