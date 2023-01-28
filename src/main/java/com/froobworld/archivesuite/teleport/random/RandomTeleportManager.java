package com.froobworld.archivesuite.teleport.random;

import com.froobworld.archivesuite.ArchiveSuite;

import java.util.HashMap;
import java.util.Map;

public class RandomTeleportManager {
    private final Map<String, MapRandomTeleportManager> mapRandomTeleportManagers = new HashMap<>();

    public RandomTeleportManager(ArchiveSuite archiveSuite) {
        for (String map : archiveSuite.getMapManager().getEnabledMaps()) {
            mapRandomTeleportManagers.put(map, new MapRandomTeleportManager(archiveSuite, map));
        }
    }

    public MapRandomTeleportManager getMapRandomTeleportManager(String mapName) {
        return mapRandomTeleportManagers.get(mapName);
    }

}
