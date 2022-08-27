package com.froobworld.archivesuite.teleport.spawn;

import com.froobworld.archivesuite.ArchiveSuite;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.*;
import java.util.UUID;

public class MapSpawnManager {
    private final ArchiveSuite archiveSuite;
    private final String map;
    private final File spawnFile;
    private Location spawnLocation;

    public MapSpawnManager(ArchiveSuite archiveSuite, String map) {
        this.archiveSuite = archiveSuite;
        this.map = map;
        this.spawnFile = new File(archiveSuite.getDataFolder().getPath() + "/spawn/" + map, "spawn.json");
        readSpawn();
    }

    public Location getSpawnLocation() {
        return spawnLocation == null ? archiveSuite.getMapManager().getMapWorlds(map).get(0).getSpawnLocation() : spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
        Bukkit.getScheduler().runTaskAsynchronously(archiveSuite, () -> writeSpawn(spawnLocation, spawnFile));
    }

    private void readSpawn() {
        if (!spawnFile.exists()) {
            spawnLocation = null;
            return;
        }
        try (JsonReader jsonReader = new JsonReader(new FileReader(spawnFile))) {
            UUID worldUuid = null;
            Double x = null;
            Double y = null;
            Double z = null;
            Float yaw = null;
            Float pitch = null;
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                switch (name) {
                    case "world" -> worldUuid = UUID.fromString(jsonReader.nextString());
                    case "x" -> x = jsonReader.nextDouble();
                    case "y" -> y = jsonReader.nextDouble();
                    case "z" -> z = jsonReader.nextDouble();
                    case "yaw" -> yaw = (float) jsonReader.nextDouble();
                    case "pitch" -> pitch = (float) jsonReader.nextDouble();
                }
            }
            jsonReader.endObject();
            //noinspection ConstantConditions
            spawnLocation = new Location(Bukkit.getWorld(worldUuid), x, y, z, yaw, pitch);
        } catch (Exception e) {
            spawnLocation = null;
        }
    }

    private static synchronized void writeSpawn(Location location, File spawnFile) {
        spawnFile.getParentFile().mkdirs();
        if (!spawnFile.exists()) {
            try {
                spawnFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (JsonWriter jsonWriter = new JsonWriter(new FileWriter(spawnFile))) {
            jsonWriter.beginObject()
                    .name("world").value(location.getWorld().getUID().toString())
                    .name("x").value(location.getX())
                    .name("y").value(location.getY())
                    .name("z").value(location.getZ())
                    .name("yaw").value(location.getYaw())
                    .name("pitch").value(location.getPitch())
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
