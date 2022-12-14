package com.froobworld.archivesuite.teleport.request;

import com.froobworld.archivesuite.ArchiveSuite;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TeleportRequestHandler {
    private final ArchiveSuite archiveSuite;
    private final Map<Player, RequestedTeleport> teleportRequestMap = new HashMap<>();

    public TeleportRequestHandler(ArchiveSuite archiveSuite) {
        this.archiveSuite = archiveSuite;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(archiveSuite, this::cleanup, 100, 100);
    }

    public RequestedTeleport getRequestedTeleport(Player subject) {
        return teleportRequestMap.get(subject);
    }

    public void requestTeleportTo(Player requester, Player subject) {
        teleportRequestMap.put(subject, new RequestedTeleport(requester, subject, RequestType.TO));
    }

    public void requestSummon(Player requester, Player subject) {
        teleportRequestMap.put(subject, new RequestedTeleport(requester, subject, RequestType.SUMMON));
    }

    private void cleanup() {
        teleportRequestMap.entrySet().removeIf(entry -> !entry.getValue().isValid());
    }

    public class RequestedTeleport {
        private final Player requester;
        private final Player subject;
        private final RequestType type;
        private final long requestTime;
        private boolean valid = true;

        private RequestedTeleport(Player requester, Player subject, RequestType type) {
            this.requester = requester;
            this.subject = subject;
            this.type = type;
            this.requestTime = System.currentTimeMillis();
        }

        public void carryOut() {
            if (!valid) {
                return;
            }
            if (type == RequestType.TO) {
                TeleportRequestHandler.this.archiveSuite.getPlayerTeleporter().teleport(requester, subject);
            } else {
                TeleportRequestHandler.this.archiveSuite.getPlayerTeleporter().teleport(subject, requester);
            }
            invalidate();
        }

        public void invalidate() {
            this.valid = false;
        }

        public Player getRequester() {
            return requester;
        }

        public Player getSubject() {
            return subject;
        }

        public boolean isValid() {
            return valid && requester.isValid() && subject.isValid() && !expired();
        }

        private boolean expired() {
            return System.currentTimeMillis() - requestTime > TimeUnit.MINUTES.toMillis(1);
        }

    }

    public enum RequestType {
        TO,
        SUMMON
    }

}
