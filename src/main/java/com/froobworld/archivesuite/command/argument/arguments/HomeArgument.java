package com.froobworld.archivesuite.command.argument.arguments;

import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import com.froobworld.archivesuite.command.argument.predicate.ArgumentPredicate;
import com.froobworld.archivesuite.map.MapManager;
import com.froobworld.archivesuite.teleport.home.Home;
import com.froobworld.archivesuite.teleport.home.HomeManager;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;

public class HomeArgument<C> extends CommandArgument<C, Home> {

    @SafeVarargs
    public HomeArgument(boolean required, @NonNull String name, HomeManager homeManager, MapManager mapManager, Function<CommandContext<C>, Player> playerContext, ArgumentPredicate<C, Home>... predicates) {
        super(required, name, new Parser<>(mapManager, playerContext, homeManager, predicates), Home.class);
    }

    @Override
    public @NonNull String getDefaultValue() {
        return "default";
    }

    private static final class Parser<C> implements ArgumentParser<C, Home> {
        private final MapManager mapManager;
        private final Function<CommandContext<C>, Player> playerContext;
        private final HomeManager homeManager;
        private final ArgumentPredicate<C, Home>[] predicates;

        @SafeVarargs
        private Parser(MapManager mapManager, Function<CommandContext<C>, Player> playerContext, HomeManager homeManager, ArgumentPredicate<C, Home>... predicates) {
            this.mapManager = mapManager;
            this.playerContext = playerContext;
            this.homeManager = homeManager;
            this.predicates = predicates;
        }

        @Override
        public @NonNull ArgumentParseResult<Home> parse(@NonNull CommandContext<C> commandContext, @NonNull Queue<String> inputQueue) {
            if (inputQueue.isEmpty()) {
                return ArgumentParseResult.failure(new NoInputProvidedException(Parser.class, commandContext));
            }
            Player player = playerContext.apply(commandContext);
            String map = mapManager.getMap(player);
            StringBuilder input = new StringBuilder(inputQueue.remove());
            while (!inputQueue.isEmpty()) {
                input.append(" ").append(inputQueue.remove());
            }
            if (map == null || homeManager.getMapHomeManager(map) == null) {
                return ArgumentParseResult.failure(new HomeNotFoundException(input.toString()));
            }
            Home home = homeManager.getMapHomeManager(map).getHomes(player).getHome(input.toString());
            if (home == null) {
                return ArgumentParseResult.failure(new HomeNotFoundException(input.toString()));
            }
            return ArgumentPredicate.testAll(commandContext, home, predicates);
        }

        @Override
        public @NonNull List<String> suggestions(@NonNull CommandContext<C> commandContext, @NonNull String input) {
            List<String> suggestions = new ArrayList<>();
            Player player = playerContext.apply(commandContext);
            String map = mapManager.getMap(player);
            if (map == null || homeManager.getMapHomeManager(map) == null) {
                return suggestions;
            }

            for (Home home : homeManager.getMapHomeManager(map).getHomes(player).getHomes()) {
                if (ArgumentPredicate.testAll(commandContext, home, predicates).getFailure().isEmpty()) {
                    suggestions.add(home.getName());
                }
            }
            return suggestions;
        }

        @Override
        public boolean isContextFree() {
            return false;
        }

    }

    public static class HomeNotFoundException extends IllegalArgumentException {

        public HomeNotFoundException(String input) {
            super("No home found for input '" + input + "'");
        }

    }

}
