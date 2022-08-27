package com.froobworld.archivesuite.teleport.portal;

import com.froobworld.archivesuite.ArchiveSuite;

public class PortalManager {

    public PortalManager(ArchiveSuite archiveSuite) {
        for (String map : archiveSuite.getMapManager().getEnabledMaps()) {
            new MapPortalManager(archiveSuite, map);
        }
    }

}
