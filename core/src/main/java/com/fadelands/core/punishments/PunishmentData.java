package com.fadelands.core.punishments;

import javax.annotation.Nullable;
import java.util.*;

public class PunishmentData {

    private UUID uuid;

    public PunishmentData(UUID uuid) {
        this.uuid = uuid;
    }

    private HashMap<UUID, List<Punishment>> punishments = new HashMap<>();

    public HashMap<UUID, List<Punishment>> getPunishments() {
        return punishments;
    }

    public void addPunishment(Punishment punishment) {
        if(!(punishments.containsKey(uuid))) {
            punishments.put(uuid, new ArrayList<>());
        }

        punishments.get(uuid).add(punishment);
    }

    @Nullable
    public Punishment getPunishment(PunishmentType type) {
        if(!(punishments.containsKey(uuid))) {
            return null;
        }
        if(punishments.get(uuid) == null) return null;

        for(Punishment punishment : punishments.get(uuid)) {
            if (type == PunishmentType.Ban && punishment.isBanned()) {
                return punishment;
            } else if (type == PunishmentType.Mute && punishment.isMuted()) {
                return punishment;
            }
        }
        return null;
    }

    public boolean isBanned() {
        if(punishments.get(uuid) == null) return false;
        for(Punishment punishments : punishments.get(uuid)) {
            if (punishments.isBanned()) {
                return true;
            }
        }
        return false;
    }

    public boolean isMuted() {
        if(punishments.get(uuid) == null) return false;
        for(Punishment punishments : punishments.get(uuid)) {
            if (punishments.isMuted()) {
                return true;
            }
        }
        return false;
    }

}