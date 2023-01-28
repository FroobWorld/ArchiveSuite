package com.froobworld.archivesuite.teleport.random;

import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.teleport.home.Home;
import com.froobworld.archivesuite.teleport.home.Homes;
import com.froobworld.archivesuite.teleport.warp.Warp;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class MapRandomTeleportManager {
    public final List<RandomTeleportCluster> teleportClusters = new ArrayList<>();

    public MapRandomTeleportManager(ArchiveSuite archiveSuite, String mapName) {
        for (Warp warp : archiveSuite.getWarpManager().getMapWarpManager(mapName).getWarps()) {
            addRandomTeleportSpot(new RandomTeleportSpot(warp));
        }
        for (Homes homes : archiveSuite.getHomeManager().getMapHomeManager(mapName).getAllHomes()) {
            for (Home home : homes.getHomes()) {
                addRandomTeleportSpot(new RandomTeleportSpot(home));
            }
        }
    }

    private void addRandomTeleportSpot(RandomTeleportSpot randomTeleportSpot) {
        RandomTeleportCluster cluster = null;
        ListIterator<RandomTeleportCluster> iterator = teleportClusters.listIterator();
        while (iterator.hasNext()) {
            RandomTeleportCluster nextCluster = iterator.next();
            if (nextCluster.isInCluster(randomTeleportSpot)) {
                if (cluster == null) {
                    cluster = nextCluster;
                    cluster.add(randomTeleportSpot);
                } else {
                    cluster.merge(nextCluster);
                    iterator.remove();
                }
            }
        }

        if (cluster == null) {
            cluster = new RandomTeleportCluster();
            cluster.add(randomTeleportSpot);
            teleportClusters.add(cluster);
        }
    }

    public RandomTeleportSpot getRandomTeleportSpot() {
        if (teleportClusters.size() == 0) {
            return null;
        }
        return teleportClusters.get(new Random().nextInt(teleportClusters.size())).randomTeleportSpot();
    }

}
