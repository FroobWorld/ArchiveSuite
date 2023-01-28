package com.froobworld.archivesuite.teleport.home;

import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.data.DataLoader;
import com.froobworld.archivesuite.data.DataSaver;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public class MapHomeManager {
    public static final Pattern homeNamePattern = Pattern.compile("^[a-zA-z0-9-_]+$");
    private static final Pattern fileNamePattern = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.json$");
    private final BiMap<UUID, Homes> homesMap = HashBiMap.create();
    protected final DataSaver homesSaver;

    public MapHomeManager(ArchiveSuite archiveSuite, String mapName) {
        File directory = new File(archiveSuite.getDataFolder(), "homes/" + mapName + "/");
        homesSaver = new DataSaver(archiveSuite, 1200);
        homesMap.putAll(DataLoader.loadAll(
                directory,
                fileName -> fileNamePattern.matcher(fileName.toLowerCase()).matches(),
                bytes -> Homes.fromJsonString(this, new String(bytes)),
                (fileName, homes) -> homes.getUuid()
        ));
        homesSaver.start();
        homesSaver.addDataType(Homes.class, homes -> homes.toJsonString().getBytes(), homes -> new File(directory, homes.getUuid().toString() + ".json"));
    }

    public void shutdown() {
        homesSaver.stop();
    }

    public Homes getHomes(Player player) {
        if (!homesMap.containsKey(player.getUniqueId())) {
            Homes homes = new Homes(this, player.getUniqueId());
            homesMap.put(player.getUniqueId(), homes);
            homesSaver.scheduleSave(homes);
        }
        return homesMap.get(player.getUniqueId());
    }

    public Home createHome(Player player, String name) {
        Home home = new Home(name, player.getLocation());
        getHomes(player).addHome(home);
        return home;
    }

    public Set<Homes> getAllHomes() {
        return homesMap.values();
    }

    public void deleteHome(Player player, Home home) {
        getHomes(player).removeHome(home);
    }

}
