package com.froobworld.archivesuite.teleport.portal;

import com.froobworld.archivesuite.data.SchemaEntries;
import com.froobworld.archivesuite.data.SimpleDataSchema;
import org.bukkit.Location;

import java.io.IOException;
import java.util.UUID;

public class Portal {
    private static final SimpleDataSchema<Portal> SCHEMA = new SimpleDataSchema.Builder<Portal>()
            .addField("name", SchemaEntries.stringEntry(
                    portal -> portal.name,
                    (portal, name) -> portal.name = name
            ))
            .addField("location", SchemaEntries.locationEntry(
                    portal -> portal.location,
                    (portal, location) -> portal.location = location
            ))
            .addField("radius", SchemaEntries.doubleEntry(
                    portal -> portal.radius,
                    (portal, radius) -> portal.radius = radius
            ))
            .addField("link", SchemaEntries.stringEntry(
                    portal -> portal.link,
                    (portal, link) -> portal.link = link
            ))
            .addField("creator", SchemaEntries.uuidEntry(
                    portal -> portal.creator,
                    (portal, creator) -> portal.creator = creator
            ))
            .addField("created", SchemaEntries.longEntry(
                    portal -> portal.created,
                    (portal, created) -> portal.created = created
            ))
            .build();

    private final MapPortalManager portalManager;
    private String name;
    private Location location;
    private double radius;
    private String link;
    private UUID creator;
    private long created;

    private Portal(MapPortalManager portalManager) {
        this.portalManager = portalManager;
    }

    public Portal(MapPortalManager portalManager, String name, Location location, double radius, UUID creator) {
        this.portalManager = portalManager;
        this.name = name;
        this.location = location;
        this.radius = radius;
        this.creator = creator;
        this.created = System.currentTimeMillis();
    }

    public Location getLocation() {
        return location;
    }

    public double getRadius() {
        return radius;
    }

    public Portal getLink() {
        if (link == null) {
            return null;
        }
        return portalManager.getPortal(link);
    }

    public void setLink(Portal portal) {
        Portal currentLink = getLink();
        if (currentLink != null) {
            currentLink.setLink(null);
        }
        this.link = portal == null ? null : portal.getName();
        if (portal != null) {
            portal.setLinkOneWay(this);
        }
        portalManager.portalSaver.scheduleSave(this);
    }

    private void setLinkOneWay(Portal portal) {
        link = portal.getName();
        portalManager.portalSaver.scheduleSave(this);
    }

    public String getName() {
        return name;
    }

    public UUID getCreator() {
        return creator;
    }

    public long getTimeCreated() {
        return created;
    }

    public String toJsonString() {
        try {
            return SCHEMA.toJsonString(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Portal fromJsonString(MapPortalManager portalManager, String jsonString) {
        Portal portal = new Portal(portalManager);
        try {
            SCHEMA.populateFromJsonString(portal, jsonString);
            return portal;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
