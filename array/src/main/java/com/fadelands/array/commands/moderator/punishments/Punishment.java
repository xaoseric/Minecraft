package com.fadelands.array.commands.moderator.punishments;

public class Punishment {

    private String appealkey;
    private int type;
    private String punisherUuid;
    private String reason;
    private String punishedUuid;
    private long date;
    private long until;
    private boolean active;
    private boolean removed;
    private String removeReason;
    private String removeAdmin;

    public Punishment(String appealkey, String punisherUuid, int type, String reason, String punishedUuid, long date, long until, boolean active, boolean removed, String removeReason, String removeAdmin) {
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

    public void build(){}

    public String getAppealKey() {
        return appealkey;
    }

    public int getPunishmentType()
    {
        return type;
    }

    public String getPunishedUuid()
    {
        return this.punishedUuid;
    }

    public String getPunisherUUID()
    {
        return this.punisherUuid;
    }

    public String getReason()
    {
        return this.reason;
    }

    public long getPunishDate() {
        return date;
    }

    public long getUntil() {
        return until;
    }

    public boolean getActive() {
        return active;
    }

    public boolean getRemoved() {
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
        return type == 0 && active;
    }

    public boolean isMuted() {
        return type == 1 && active;
    }

    public long getRemaining() {
        return System.currentTimeMillis() - until;
    }

    public String getRemoveAdmin() {
        return removeAdmin;
    }
}
