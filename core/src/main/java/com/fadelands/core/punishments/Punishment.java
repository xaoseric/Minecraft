package com.fadelands.core.punishments;

import java.util.UUID;

public class Punishment {

    private String appealkey;
    private PunishmentType type;
    private UUID punisherUuid;
    private String reason;
    private UUID punishedUuid;
    private long date;
    private long until;
    private boolean active;
    private boolean removed;
    private String removeReason;
    private String removeAdmin;

    Punishment(String appealkey, UUID punisherUuid, PunishmentType type, String reason, UUID punishedUuid, long date, long until, boolean active, boolean removed, String removeReason, String removeAdmin) {
        this.appealkey = appealkey;
        this.punisherUuid = punisherUuid;
        this.type = type;
        this.reason = reason;
        this.punishedUuid = punishedUuid;
        this.date = date;
        this.until = until;
        this.active = active;
        this.removed = removed;
        this.removeReason = removeReason;
        this.removeAdmin = removeAdmin;
    }

    public String getAppealKey() {
        return appealkey;
    }

    public PunishmentType getPunishmentType() {
        return type;
    }

    public UUID getPunishedUuid() {
        return this.punishedUuid;
    }

    public UUID getPunisherUuid()
    {
        return this.punisherUuid;
    }

    public String getReason()
    {
        return this.reason;
    }

    public long getPunishTime() {
        return date;
    }

    public long getExpirationTime() {
        return until;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void remove(String admin, String reason) {
        removed = true;
        removeAdmin = admin;
        removeReason = reason;
    }

    public String getRemoveReason()
    {
        return  removeReason;
    }

    public boolean isBanned() {
        return type == PunishmentType.Ban && active;
    }

    public boolean isMuted() {
        return type == PunishmentType.Mute && active;
    }

    public long getRemaining() {
        return (date + until) - System.currentTimeMillis();
    }

    public String getRemoveAdmin() {
        return removeAdmin;
    }
}
