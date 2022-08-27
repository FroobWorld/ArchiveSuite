package com.froobworld.archivesuite.motd;

import com.froobworld.archivesuite.ArchiveSuite;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.stream.Collectors;


public class MotdManager implements Listener {
    private final ArchiveSuite archiveSuite;

    public MotdManager(ArchiveSuite archiveSuite) {
        this.archiveSuite = archiveSuite;
        Bukkit.getPluginManager().registerEvents(this, archiveSuite);
    }

    public List<Component> getMotd() {
        return archiveSuite.getArchiveSuiteConfig().motd.get().stream()
                .map(string -> MiniMessage.miniMessage().deserialize(string))
                .collect(Collectors.toList());
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(archiveSuite, () -> getMotd().forEach(event.getPlayer()::sendMessage), 10);
    }

}
