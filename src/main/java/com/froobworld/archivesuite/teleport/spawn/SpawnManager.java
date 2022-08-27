package com.froobworld.archivesuite.teleport.spawn;

import com.froobworld.archivesuite.ArchiveSuite;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;

public class SpawnManager implements Listener {
    private final ArchiveSuite archiveSuite;
    private final Map<String, MapSpawnManager> mapSpawnManagers;
    private final MapSpawnManager defaultSpawnManager;


    public SpawnManager(ArchiveSuite archiveSuite) {
        this.archiveSuite = archiveSuite;
        Bukkit.getPluginManager().registerEvents(this, archiveSuite);
        defaultSpawnManager = new MapSpawnManager(archiveSuite, Bukkit.getWorlds().get(0).getName());
        mapSpawnManagers = new HashMap<>();
        for (String map : archiveSuite.getMapManager().getEnabledMaps()) {
            mapSpawnManagers.put(map, new MapSpawnManager(archiveSuite, map));
        }
    }

    public MapSpawnManager getMapSpawnManager(String map) {
        if (map == null) {
            return defaultSpawnManager;
        }
        return mapSpawnManagers.get(map);
    }

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        MapSpawnManager mapSpawnManager = getMapSpawnManager(archiveSuite.getMapManager().getMap(event.getPlayer()));
        if (mapSpawnManager == null) {
            mapSpawnManager = defaultSpawnManager;
        }
        event.setRespawnLocation(mapSpawnManager.getSpawnLocation());
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
            MapSpawnManager mapSpawnManager = getMapSpawnManager(archiveSuite.getMapManager().getMap(event.getPlayer()));
            if (mapSpawnManager == null) {
                mapSpawnManager = defaultSpawnManager;
            }
            event.getPlayer().teleport(mapSpawnManager.getSpawnLocation());
        }
    }
}
