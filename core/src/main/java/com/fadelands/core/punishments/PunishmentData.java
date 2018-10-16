package com.fadelands.core.punishments;

import com.fadelands.core.Core;
import com.fadelands.core.utils.Callback;

import java.util.*;

public class PunishmentData {

    private List<Punishment> punishments = new ArrayList<>();
    private final UUID uuid;

    public PunishmentData(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public void addPunishment(Punishment punishment) {
        punishments.add(punishment);
    }

    public List<Punishment> getPunishments() {
        return punishments;
    }

    public List<Punishment> getPunishments(PunishmentType type) {
        List<Punishment> list = new ArrayList<>();

        for(Punishment punishment : punishments) {
            if (punishment.getType() == type) list.add(punishment);
        }
        return list;
    }

    public List<Punishment> getActivePunishments(PunishmentType type) {
        List<Punishment> list = new ArrayList<>();

        for(Punishment punishment : punishments) {
            if (punishment.getType() == type && punishment.isActive()) list.add(punishment);
        }
        return list;
    }

    public boolean hasActive(PunishmentType type) {
        return getActivePunishments(type).size() > 0;
    }

    public static void load(UUID uuid, Callback<PunishmentData> callback) {
        Core.plugin.getPunishmentManager().loadPunishments(uuid, callback);
    }
}