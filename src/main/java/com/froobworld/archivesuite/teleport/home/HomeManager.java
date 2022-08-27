package com.froobworld.archivesuite.teleport.home;

import com.froobworld.archivesuite.ArchiveSuite;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HomeManager {
    public static final Pattern homeNamePattern = Pattern.compile("^[a-zA-z0-9-_]+$");
    private final ArchiveSuite archiveSuite;
    private final Map<String, MapHomeManager> mapHomeManagers = new HashMap<>();

    public HomeManager(ArchiveSuite archiveSuite) {
        this.archiveSuite = archiveSuite;
        load();
    }

    private void load() {
        for (String map : archiveSuite.getMapManager().getEnabledMaps()) {
            MapHomeManager mapHomeManager = new MapHomeManager(archiveSuite, map);
            mapHomeManagers.put(map, mapHomeManager);
        }
    }

    public MapHomeManager getMapHomeManager(String map) {
        if (map == null) {
            return null;
        }
        return mapHomeManagers.get(map);
    }

}
