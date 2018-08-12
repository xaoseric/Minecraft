package com.fadelands.array.commands.moderator.punishments;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class Punishment {

    private final UUID punisher;
    private final OfflinePlayer target;
    private final PunishmentType type;

    private IReason reason = new CustomReason() {
        @Override
        public String getMessage() {
            return " ";
        }
    };

    public Punishment(OfflinePlayer targetName, UUID punisher, PunishmentType type) {
        this.target = targetName;
        this.punisher = punisher;
        this.type = type;
    }

    public String getTargetName() {
        return this.target.getName();
    }

    public UUID getPunisherUUID() {
        return this.punisher;
    }

    public PunishmentType getType() {
        return this.type;
    }

    public IReason getReason() {
        return this.reason;
    }

    public void setReason(IReason reason){
        this.reason = reason;
    }

    public enum PunishmentType {
        BAN, KICK, MUTE, TEMP_BAN;
    }
}
