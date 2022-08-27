package com.froobworld.archivesuite.command.argument.arguments;

import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import com.froobworld.archivesuite.command.argument.predicate.ArgumentPredicate;
import com.froobworld.archivesuite.map.MapManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MapArgument<C> extends CommandArgument<C, String> {

    @SafeVarargs
    public MapArgument(boolean required, @NonNull String name, MapManager mapManager, ArgumentPredicate<C, String>... predicates) {
        super(required, name, new Parser<>(mapManager, predicates), String.class);
    }

    private static final class Parser<C> implements ArgumentParser<C, String> {
        private final MapManager mapManager;
        private final ArgumentPredicate<C, String>[] predicates;

        @SafeVarargs
        private Parser(MapManager mapManager, ArgumentPredicate<C, String>... predicates) {
            this.mapManager = mapManager;
            this.predicates = predicates;
        }

        @Override
        public @NonNull ArgumentParseResult<String> parse(@NonNull CommandContext<C> commandContext, @NonNull Queue<String> inputQueue) {
            if (inputQueue.isEmpty()) {
                return ArgumentParseResult.failure(new NoInputProvidedException(Parser.class, commandContext));
            }
            String input = inputQueue.remove();
            if (mapManager.getMapWorlds(input) == null) {
                return ArgumentParseResult.failure(new MapNotFoundException(input));
            }
            return ArgumentPredicate.testAll(commandContext, input, predicates);
        }

        @Override
        public @NonNull List<String> suggestions(@NonNull CommandContext<C> commandContext, @NonNull String input) {
            List<String> suggestions = new ArrayList<>();
            for (String map : mapManager.getEnabledMaps()) {
                if (ArgumentPredicate.testAll(commandContext, map, predicates).getFailure().isEmpty()) {
                    suggestions.add(map);
                }
            }

            return suggestions;
        }

        @Override
        public boolean isContextFree() {
            return false;
        }

    }

    public static class MapNotFoundException extends IllegalArgumentException {

        public MapNotFoundException(String input) {
            super("No map found for input '" + input + "'");
        }

    }

}
