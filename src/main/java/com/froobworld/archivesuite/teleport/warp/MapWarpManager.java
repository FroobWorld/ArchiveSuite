package com.froobworld.archivesuite.teleport.warp;

import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.data.DataLoader;
import com.froobworld.archivesuite.data.DataSaver;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.File;
import java.util.Set;
import java.util.regex.Pattern;

public class MapWarpManager {
    public static final Pattern warpNamePattern = Pattern.compile("^[a-zA-z0-9-_]+$");
    private static final Pattern fileNamePattern = Pattern.compile(".*\\.json$");
    private final BiMap<String, Warp> warpMap = HashBiMap.create();
    protected final DataSaver warpSaver;

    public MapWarpManager(ArchiveSuite archiveSuite, String mapName) {
        File directory = new File(archiveSuite.getDataFolder(), "warps/" + mapName + "/");
        warpSaver = new DataSaver(archiveSuite, 1200);
        warpMap.putAll(DataLoader.loadAll(
                directory,
                fileName -> fileNamePattern.matcher(fileName.toLowerCase()).matches(),
                bytes -> Warp.fromJsonString(new String(bytes)),
                (fileName, warp) -> warp.getName()
        ));
        warpSaver.start();
        warpSaver.addDataType(Warp.class, warp -> warp.toJsonString().getBytes(), warp -> new File(directory, warp.getName() + ".json"));
    }

    public void shutdown() {
        warpSaver.stop();
    }

    public Set<Warp> getWarps() {
        return warpMap.values();
    }

    public Warp getWarp(String name) {
        return warpMap.get(name);
    }

}
