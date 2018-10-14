package com.fadelands.core.punishments;

import javax.annotation.Nullable;

public enum PunishmentType {

    Ban(1),
    Mute(2),
    Kick(3);

    private int id;

    PunishmentType(int id) {
        this.id = id;
    }

    @Nullable
    public static PunishmentType getById(int id) {
        for(PunishmentType type : values()) {
            if(type.getId() == id) {
                return type;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
