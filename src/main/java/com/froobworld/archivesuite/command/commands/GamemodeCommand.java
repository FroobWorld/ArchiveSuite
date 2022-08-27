package com.froobworld.archivesuite.command.commands;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.context.CommandContext;
import com.froobworld.archivesuite.command.NabCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand extends NabCommand {

    public GamemodeCommand() {
        super(
                "gamemode",
                "Change your gamemode.",
                "archivesuite.command.gamemode",
                Player.class,
                "gm"
        );
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        Player player = (Player) context.getSender();
        GameMode gameMode = context.get("gamemode");
        player.setGameMode(gameMode);
        player.sendMessage(Component.text("Changed gamemode to " + gameMode.name().toLowerCase() + ".", NamedTextColor.YELLOW));
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder
                .argument(EnumArgument.newBuilder(GameMode.class, "gamemode"));
    }
}
