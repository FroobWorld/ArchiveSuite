package com.froobworld.archivesuite.teleport.random;

import com.froobworld.archivesuite.teleport.home.Home;
import com.froobworld.archivesuite.teleport.warp.Warp;
import org.bukkit.Location;

public class RandomTeleportSpot {
    private final Home home;
    private final Warp warp;

    public RandomTeleportSpot(Home home) {
        this.home = home;
        this.warp = null;
    }

    public RandomTeleportSpot(Warp warp) {
        this.home = null;
        this.warp = warp;
    }

    public Location getLocation() {
        if (home != null) {
            return home.getLocation();
        }
        if (warp != null) {
            return warp.getLocation();
        }
        throw new IllegalStateException();
    }


}
