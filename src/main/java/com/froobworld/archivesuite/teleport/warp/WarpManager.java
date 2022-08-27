package com.froobworld.archivesuite.teleport.warp;

import com.froobworld.archivesuite.ArchiveSuite;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class WarpManager {
    public static final Pattern warpNamePattern = Pattern.compile("^[a-zA-z0-9-_]+$");
    private final ArchiveSuite archiveSuite;
    private final Map<String, MapWarpManager> mapWarpManagers = new HashMap<>();

    public WarpManager(ArchiveSuite archiveSuite) {
        this.archiveSuite = archiveSuite;
        load();
    }

    private void load() {
        for (String map : archiveSuite.getMapManager().getEnabledMaps()) {
            MapWarpManager mapWarpManager = new MapWarpManager(archiveSuite, map);
            mapWarpManagers.put(map, mapWarpManager);
        }
    }

    public MapWarpManager getMapWarpManager(String map) {
        if (map == null) {
            return null;
        }
        return mapWarpManagers.get(map);
    }

}
