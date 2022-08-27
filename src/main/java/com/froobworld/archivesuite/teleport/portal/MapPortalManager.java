package com.froobworld.archivesuite.teleport.portal;

import com.froobworld.archivesuite.ArchiveSuite;
import com.froobworld.archivesuite.data.DataLoader;
import com.froobworld.archivesuite.data.DataSaver;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Set;
import java.util.regex.Pattern;

public class MapPortalManager {
    public static final Pattern portalNamePattern = Pattern.compile("^[a-zA-z0-9-_]+$");
    private static final Pattern fileNamePattern = Pattern.compile(".*\\.json$");
    protected final DataSaver portalSaver;
    private final BiMap<String, Portal> portalMap = HashBiMap.create();
    private final File directory;
    private final PortalEnforcer portalEnforcer;

    public MapPortalManager(ArchiveSuite archiveSuite, String map) {
        directory = new File(archiveSuite.getDataFolder(), "portals/" + map + "/");
        portalSaver = new DataSaver(archiveSuite, 1200);
        portalMap.putAll(DataLoader.loadAll(
                directory,
                fileName -> fileNamePattern.matcher(fileName.toLowerCase()).matches(),
                bytes -> Portal.fromJsonString(this, new String(bytes)),
                (fileName, portal) -> portal.getName().toLowerCase()
        ));
        portalSaver.start();
        portalSaver.addDataType(Portal.class, portal -> portal.toJsonString().getBytes(), portal -> new File(directory, portal.getName() + ".json"));
        this.portalEnforcer = new PortalEnforcer(archiveSuite, this);
    }

    public void shutdown() {
        portalSaver.stop();
    }

    public Portal createPortal(String name, double radius, Player creator) {
        if (!portalNamePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name does not match pattern: " + portalNamePattern);
        }
        if (portalMap.containsKey(name.toLowerCase())) {
            throw new IllegalStateException("Portal with that name already exists");
        }
        Portal portal = new Portal(this, name, creator.getLocation(), radius, creator.getUniqueId());
        portalMap.put(name.toLowerCase(), portal);
        portalSaver.scheduleSave(portal);
        return portal;
    }

    public void deletePortal(Portal portal) {
        portalMap.remove(portal.getName().toLowerCase());
        portalSaver.scheduleDeletion(portal);
    }

    public Portal getPortal(String name) {
        return portalMap.get(name.toLowerCase());
    }

    public Set<Portal> getPortals() {
        return portalMap.values();
    }

    public PortalEnforcer getPortalEnforcer() {
        return portalEnforcer;
    }

}
