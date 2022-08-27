package com.froobworld.archivesuite.teleport.portal;

import com.froobworld.archivesuite.ArchiveSuite;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PortalEnforcer {
    private final MapPortalManager portalManager;
    private final Set<UUID> immunePlayers = new HashSet<>();

    public PortalEnforcer(ArchiveSuite archiveSuite, MapPortalManager portalManager) {
        this.portalManager = portalManager;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(archiveSuite, this::loop, 10, 10);
    }

    private void loop() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            boolean inPortal = false;
            for (Portal portal : portalManager.getPortals()) {
                if (portal.getLink() == null) {
                    continue;
                }
                if (!portal.getLocation().getWorld().equals(player.getWorld())) {
                    continue;
                }
                if (player.getLocation().distanceSquared(portal.getLocation()) <= portal.getRadius() * portal.getRadius()) {
                    inPortal = true;
                    handleInPortal(player, portal);
                    break;
                }
            }
            if (!inPortal) {
                immunePlayers.remove(player.getUniqueId());
            }
        }
    }

    private void handleInPortal(Player player, Portal portal) {
        if (!immunePlayers.contains(player.getUniqueId())) {
            Location destination = portal.getLink().getLocation().clone();
            player.teleport(destination);
            setPortalImmune(player);
        }
    }

    public void setPortalImmune(Player player) {
        immunePlayers.add(player.getUniqueId());
    }

}
