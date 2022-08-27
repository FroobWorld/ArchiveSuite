package com.froobworld.archivesuite.chat;

import com.froobworld.archivesuite.ArchiveSuite;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatFormatter implements Listener {

    public ChatFormatter(ArchiveSuite archiveSuite) {
        Bukkit.getPluginManager().registerEvents(this, archiveSuite);
    }

    @EventHandler(ignoreCancelled = true)
    private void onChat(AsyncChatEvent event) {
        event.renderer(
                ChatRenderer.viewerUnaware((source, sourceDisplayName, message) -> {
                    return Component.text(source.getName(), NamedTextColor.DARK_GRAY)
                            .append(Component.text(": ", NamedTextColor.WHITE))
                            .append(message.color(NamedTextColor.WHITE));
                })
        );
    }

}
