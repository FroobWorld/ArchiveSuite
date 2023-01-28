package com.froobworld.archivesuite.teleport.random;

import java.util.*;

public class RandomTeleportCluster {
    private static final int CLUSTER_DISTANCE_SQUARED = 100 * 100;
    private final List<RandomTeleportSpot> teleportSpots = new ArrayList<>();

    public boolean isInCluster(RandomTeleportSpot randomTeleportSpot) {
        for (RandomTeleportSpot teleportSpot : teleportSpots) {
            if (!teleportSpot.getLocation().getWorld().equals(randomTeleportSpot.getLocation().getWorld())) {
                continue;
            }
            if (teleportSpot.getLocation().distanceSquared(randomTeleportSpot.getLocation()) < CLUSTER_DISTANCE_SQUARED) {
                return true;
            }
        }
        return false;
    }

    public void add(RandomTeleportSpot randomTeleportSpot) {
        this.teleportSpots.add(randomTeleportSpot);
    }

    public void merge(RandomTeleportCluster otherCluster) {
        teleportSpots.addAll(otherCluster.teleportSpots);
    }

    public RandomTeleportSpot randomTeleportSpot() {
        if (teleportSpots.size() == 0) {
            throw new IllegalStateException();
        }
        return teleportSpots.get(new Random().nextInt(teleportSpots.size()));
    }

}
