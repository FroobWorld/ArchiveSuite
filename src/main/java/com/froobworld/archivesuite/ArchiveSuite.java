package com.froobworld.archivesuite;

import com.froobworld.archivesuite.chat.ChatFormatter;
import com.froobworld.archivesuite.command.NabCommandManager;
import com.froobworld.archivesuite.command.commands.*;
import com.froobworld.archivesuite.config.ArchiveSuiteConfig;
import com.froobworld.archivesuite.gamemode.GamemodeManager;
import com.froobworld.archivesuite.map.MapManager;
import com.froobworld.archivesuite.teleport.BackManager;
import com.froobworld.archivesuite.teleport.PlayerTeleporter;
import com.froobworld.archivesuite.teleport.home.HomeManager;
import com.froobworld.archivesuite.teleport.portal.PortalManager;
import com.froobworld.archivesuite.teleport.request.TeleportRequestHandler;
import com.froobworld.archivesuite.teleport.spawn.SpawnManager;
import com.froobworld.archivesuite.teleport.warp.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ArchiveSuite extends JavaPlugin {
    private ArchiveSuiteConfig config;
    private NabCommandManager nabCommandManager;
    private MapManager mapManager;
    private BackManager backManager;
    private PlayerTeleporter playerTeleporter;
    private HomeManager homeManager;
    private WarpManager warpManager;
    private SpawnManager spawnManager;
    private TeleportRequestHandler teleportRequestHandler;

    @Override
    public void onEnable() {
        try {
            nabCommandManager = new NabCommandManager(this);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        config = new ArchiveSuiteConfig(this);
        try {
            config.load();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

        mapManager = new MapManager(this);
        new GamemodeManager(this);
        backManager = new BackManager(this);
        playerTeleporter = new PlayerTeleporter(this);
        homeManager = new HomeManager(this);
        warpManager = new WarpManager(this);
        spawnManager = new SpawnManager(this);
        new PortalManager(this);
        teleportRequestHandler = new TeleportRequestHandler(this);
        new ChatFormatter(this);

        List.of(
                new MapCommand(this),
                new MapsCommand(this),
                new GamemodeCommand(),
                new TeleportCoordsCommand(this),
                new BackCommand(this),
                new HomeCommand(this),
                new HomesCommand(this),
                new WarpCommand(this),
                new WarpsCommand(this),
                new SpawnCommand(this),
                new SetSpawnCommand(this),
                new TeleportRequestCommand(this),
                new TeleportHereRequestCommand(this),
                new TeleportAcceptCommand(this),
                new TeleportDenyCommand(this)
        ).forEach(nabCommandManager::registerCommand);
    }

    @Override
    public void onDisable() {

    }

    public ArchiveSuiteConfig getArchiveSuiteConfig() {
        return config;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public BackManager getBackManager() {
        return backManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public PlayerTeleporter getPlayerTeleporter() {
        return playerTeleporter;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public TeleportRequestHandler getTeleportRequestHandler() {
        return teleportRequestHandler;
    }
}
