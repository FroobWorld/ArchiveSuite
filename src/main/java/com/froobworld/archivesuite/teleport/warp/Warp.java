package com.froobworld.archivesuite.teleport.warp;

import com.froobworld.archivesuite.data.SchemaEntries;
import com.froobworld.archivesuite.data.SimpleDataSchema;
import org.bukkit.Location;

import java.io.IOException;

public class Warp {
    static final SimpleDataSchema<Warp> SCHEMA = new SimpleDataSchema.Builder<Warp>()
            .addField("name", SchemaEntries.stringEntry(
                    home -> home.name,
                    (home, name) -> home.name = name
            ))
            .addField("location", SchemaEntries.locationEntry(
                    home -> home.location,
                    (home, location) -> home.location = location
            ))
            .addField("created", SchemaEntries.longEntry(
                    home -> home.created,
                    (home, created) -> home.created = created
            ))
            .build();

    private String name;
    private Location location;
    private long created;

    private Warp() {}

    public Warp(String name, Location location) {
        this.name = name;
        this.location = location;
        this.created = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
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

    static Warp fromJsonString(String jsonString) {
        Warp warp = new Warp();
        try {
            SCHEMA.populateFromJsonString(warp, jsonString);
            return warp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
