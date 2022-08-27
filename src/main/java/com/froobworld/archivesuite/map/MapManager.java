package com.froobworld.archivesuite.map;

import com.froobworld.archivesuite.ArchiveSuite;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class MapManager {
    private final ArchiveSuite archiveSuite;
    private final Map<String, List<World>> enabledMaps = new HashMap<>();

    public MapManager(ArchiveSuite archiveSuite) {
        this.archiveSuite = archiveSuite;
        load();
    }

    private void load() {
        List<String> mapsToLoad = archiveSuite.getArchiveSuiteConfig().enabledMaps.get();
        String netherSuffix = "_nether";
        String endSuffix = "_the_end";

        for (String mapName : mapsToLoad) {
            String[] worldArray = new String[] {mapName, mapName + netherSuffix, mapName + endSuffix};
            for (String world : worldArray) {
                if (new File(Bukkit.getWorldContainer(), world + "/").exists()) {
                    World.Environment environment;
                    if (world.endsWith(netherSuffix)) {
                        environment = World.Environment.NETHER;
                    } else if (world.endsWith(endSuffix)) {
                        environment = World.Environment.THE_END;
                    } else {
                        environment = World.Environment.NORMAL;
                    }
                    World loadedWorld = WorldCreator.name(world).environment(environment).createWorld();
                    enabledMaps.putIfAbsent(mapName.toLowerCase(), new ArrayList<>());
                    enabledMaps.get(mapName.toLowerCase()).add(loadedWorld);
                    archiveSuite.getLogger().info("Loaded " + loadedWorld.getName() + " (" + loadedWorld.getUID() + ")");
                }
            }
        }
    }

    public Set<String> getEnabledMaps() {
        return enabledMaps.keySet();
    }

    public List<World> getMapWorlds(String map) {
        return enabledMaps.get(map.toLowerCase());
    }

    public String getMap(Player player) {
        for (String mapName : getEnabledMaps()) {
            for (World world : getMapWorlds(mapName)) {
                if (world.equals(player.getWorld())) {
                    return mapName;
                }
            }
        }
        return null;
    }

}
