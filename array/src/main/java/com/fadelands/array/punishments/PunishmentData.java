package com.fadelands.array.punishments;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PunishmentData {

    private static final List<PunishmentData> current = new ArrayList<>();

    private List<Punishment> punishments = new ArrayList<>();
    private final UUID uuid;

    public PunishmentData(UUID uuid) {
        this.uuid = uuid;

        current.add(this);
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public static PunishmentData get(UUID uuid) {
        for (int i = 0; i < current.size(); i++) {
            PunishmentData cur = current.get(i);
            if (cur.getUniqueId().equals(uuid)) return cur;
        }
        return null;
    }
}
