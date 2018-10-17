package com.fadelands.core.staff.data;

import java.util.HashMap;
import java.util.UUID;

public class StaffData {
    private static final HashMap<UUID, StaffData> all = new HashMap<>();

    private final UUID uuid;
    private final ModStats stats = new ModStats();

    public StaffData(UUID uuid) {
        this.uuid = uuid;

        all.put(uuid, this);
    }

    public void remove() {
        all.remove(uuid, this);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public ModStats getStats() {
        return this.stats;
    }

    public static HashMap<UUID, StaffData> getAll() {
        return all;
    }

    public static StaffData get(UUID uuid) {
        return all.get(uuid);
    }

    public final class ModStats {

        private int bans = 0, mutes = 0, serverReports = 0;

        public int getBans() {
            return bans;
        }

        public void setBans(int bans) {
            this.bans = bans;
        }

        public int getMutes() {
            return mutes;
        }

        public void setMutes(int mutes) {
            this.mutes = mutes;
        }

        public int getReportsHandled() {
            return serverReports;
        }

        public void setReportsHandled(int reportsHandled){
            this.serverReports = reportsHandled;
        }
    }
}
