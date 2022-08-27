package com.froobworld.archivesuite.command.argument.arguments;

import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import com.froobworld.archivesuite.command.argument.predicate.ArgumentPredicate;
import com.froobworld.archivesuite.map.MapManager;
import com.froobworld.archivesuite.teleport.warp.Warp;
import com.froobworld.archivesuite.teleport.warp.WarpManager;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;

public class WarpArgument<C> extends CommandArgument<C, Warp> {

    @SafeVarargs
    public WarpArgument(boolean required, @NonNull String name, WarpManager warpManager, MapManager mapManager, Function<CommandContext<C>, Player> playerContext, ArgumentPredicate<C, Warp>... predicates) {
        super(required, name, new Parser<>(mapManager, playerContext, warpManager, predicates), Warp.class);
    }

    private static final class Parser<C> implements ArgumentParser<C, Warp> {
        private final MapManager mapManager;
        private final Function<CommandContext<C>, Player> playerContext;
        private final WarpManager warpManager;
        private final ArgumentPredicate<C, Warp>[] predicates;

        @SafeVarargs
        private Parser(MapManager mapManager, Function<CommandContext<C>, Player> playerContext, WarpManager warpManager, ArgumentPredicate<C, Warp>... predicates) {
            this.mapManager = mapManager;
            this.playerContext = playerContext;
            this.warpManager = warpManager;
            this.predicates = predicates;
        }

        @Override
        public @NonNull ArgumentParseResult<Warp> parse(@NonNull CommandContext<C> commandContext, @NonNull Queue<String> inputQueue) {
            if (inputQueue.isEmpty()) {
                return ArgumentParseResult.failure(new NoInputProvidedException(Parser.class, commandContext));
            }
            Player player = playerContext.apply(commandContext);
            String map = mapManager.getMap(player);
            StringBuilder input = new StringBuilder(inputQueue.remove());
            while (!inputQueue.isEmpty()) {
                input.append(" ").append(inputQueue.remove());
            }
            if (map == null || warpManager.getMapWarpManager(map) == null) {
                return ArgumentParseResult.failure(new WarpNotFoundException(input.toString()));
            }
            Warp warp = warpManager.getMapWarpManager(map).getWarp(input.toString());
            if (warp == null) {
                return ArgumentParseResult.failure(new WarpNotFoundException(input.toString()));
            }
            return ArgumentPredicate.testAll(commandContext, warp, predicates);
        }

        @Override
        public @NonNull List<String> suggestions(@NonNull CommandContext<C> commandContext, @NonNull String input) {
            List<String> suggestions = new ArrayList<>();
            Player player = playerContext.apply(commandContext);
            String map = mapManager.getMap(player);
            if (map == null || warpManager.getMapWarpManager(map) == null) {
                return suggestions;
            }

            for (Warp warp : warpManager.getMapWarpManager(map).getWarps()) {
                if (ArgumentPredicate.testAll(commandContext, warp, predicates).getFailure().isEmpty()) {
                    suggestions.add(warp.getName());
                }
            }
            return suggestions;
        }

        @Override
        public boolean isContextFree() {
            return false;
        }

    }

    public static class WarpNotFoundException extends IllegalArgumentException {

        public WarpNotFoundException(String input) {
            super("No warp found for input '" + input + "'");
        }

    }

}
