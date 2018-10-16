package com.fadelands.core.punishments;

import com.fadelands.core.Core;

import java.sql.*;
import java.util.UUID;

public class PunishmentHandler {

    public PunishmentHandler() {

    }

    // Add punishment to database.
    public void punish(String appealKey, UUID punisherUuid, PunishmentType punishType, String reason, UUID punishedUuid, long date, long until, boolean permanent) {
        Connection connection = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO punishments (appeal_key, punisher_uuid, punish_type, reason, punished_uuid, date, until, permanent) " +
                "VALUES (?,?,?,?,?,?,?,?)";
        try {
            connection = Core.plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, appealKey);
            ps.setString(2, String.valueOf(punisherUuid));
            ps.setInt(3, punishType.getId());
            ps.setString(4, reason);
            ps.setString(5, String.valueOf(punishedUuid));
            ps.setLong(6, date);
            ps.setLong(7, until);
            ps.setBoolean(8, permanent);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Core.plugin.getDatabaseManager().closeComponents(ps, connection);
        }
    }

    public void unpunish(String appealKey) {
        Connection connection = null;
        PreparedStatement ps = null;
        String query = "UPDATE punishments SET active=? WHERE appeal_key = ?";
        try {
            connection = Core.plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setBoolean(1, false);
            ps.setString(2, appealKey);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Core.plugin.getDatabaseManager().closeComponents(ps, connection);
        }
    }

    public void removePunishment(String appealKey, String removalReason, UUID remover) {

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = Core.plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement("UPDATE punishments SET active=?, removed=?, remove_reason=?, remove_admin=? WHERE appeal_key='" + appealKey + "'");
            ps.setBoolean(1, false);
            ps.setBoolean(2, true);
            ps.setString(3, removalReason);
            ps.setString(4, remover.toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Core.plugin.getDatabaseManager().closeComponents(ps,connection);
        }
    }
}
