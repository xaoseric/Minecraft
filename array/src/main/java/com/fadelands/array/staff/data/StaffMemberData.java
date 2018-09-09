package com.fadelands.array.staff.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StaffMemberData {
    private static final List<StaffMemberData> all = new ArrayList<>();

    private final UUID uuid;
    private final ModStats stats = new ModStats();

    public StaffMemberData(UUID uuid) {
        this.uuid = uuid;

        all.add(this);
    }

    public void remove() {
        all.remove(this);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public ModStats getStats() {
        return this.stats;
    }

    public static List<StaffMemberData> getAll() {
        return all;
    }

    public static StaffMemberData get(UUID uuid) {
        for (int i = 0; i < all.size(); i++)
            if (all.get(i).getUUID() == uuid) return all.get(i);
        return null;
    }

    public final class ModStats {

        private int amount_bans = 0, amount_mutes = 0, amount_kicks = 0, serverReports_handled = 0;

        public int getBans() {
            return amount_bans;
        }

        public void setBans(int amount_bans) {
            this.amount_bans = amount_bans;
        }

        public int getMutes() {
            return amount_mutes;
        }

        public void setMutes(int amount_mutes) {
            this.amount_mutes = amount_mutes;
        }

        public int getKicks() {
            return amount_kicks;
        }

        public void setKicks(int amount_kicks) {
            this.amount_kicks = amount_kicks;
        }

        public int getServerReports() {
            return serverReports_handled;
        }
        public void setServerReports(int serverReports_handled){
            this.serverReports_handled = serverReports_handled;
        }
    }
}
