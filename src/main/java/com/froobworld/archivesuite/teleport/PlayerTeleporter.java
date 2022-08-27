package com.froobworld.archivesuite.teleport;

import com.froobworld.archivesuite.ArchiveSuite;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class PlayerTeleporter {
    private final ArchiveSuite archiveSuite;

    public PlayerTeleporter(ArchiveSuite archiveSuite) {
        this.archiveSuite = archiveSuite;
    }

    public void teleport(Player player, Location location) {
        archiveSuite.getBackManager().setBackLocation(player, player.getLocation());
        player.teleport(location);
    }

    public void teleport(Player player, Entity entity) {
        teleport(player, entity.getLocation());
    }

    public CompletableFuture<Location> teleportAsync(Player player, Location location) {
        return location.getWorld().getChunkAtAsync(location).thenApply(chunk -> {
            teleport(player, location);
            return location;
        });
    }

}
