package com.fadelands.array.punishments;

import com.fadelands.array.Array;
import com.fadelands.array.punishments.tokens.AddPunishToken;
import com.fadelands.array.punishments.tokens.PunishClientToken;
import com.fadelands.array.punishments.tokens.RemovePunishToken;

import java.sql.*;

public class PunishmentHandler {

    public PunishmentHandler() {

    }

    // Add punishment to database.
    public void punish(String appealKey, String punisherUuid, PunishmentType punishType, String reason, String punishedUuid, long date, long until) {
        AddPunishToken token = new AddPunishToken();
        token.appealKey = appealKey;
        token.punisherUuid = punisherUuid;
        token.punishType = punishType;
        token.reason = reason;
        token.punishedUuid = punishedUuid;
        token.date = date;
        token.until = until;

        Connection connection = null;
        PreparedStatement ps = null;
        String query = "INSERT INTO punishments (appeal_key, punisher_uuid, punish_type, reason, punished_uuid, date, until) " +
                "VALUES (?,?,?,?,?,?,?)";
        try {
            connection = Array.plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, appealKey);
            ps.setString(2, punisherUuid);
            ps.setInt(3, punishType.ordinal());
            ps.setString(4, reason);
            ps.setString(5, punishedUuid);
            ps.setLong(6, date);
            ps.setLong(7, until);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.plugin.getDatabaseManager().closeComponents(ps, connection);
        }
    }

    public void removePunishment(int punishmentId, String target, String removalReason, String remover) {
        RemovePunishToken token = new RemovePunishToken();
        token.id = punishmentId;
        token.target = target;
        token.removalReason = removalReason;
        token.removeAdmin = remover;

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = Array.plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement("UPDATE punishments SET (active,removed,remove_reason,remove_admin) WHERE report_id='" + punishmentId + "'");
            ps.setBoolean(1, false);
            ps.setBoolean(2, true);
            ps.setString(3, removalReason);
            ps.setString(4, remover);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.plugin.getDatabaseManager().closeComponents(connection);
            Array.plugin.getDatabaseManager().closeComponents(ps);
        }
    }

    // Loading users punishments.
    public PunishClientToken loadPunishClient(String targetUuid) {
        PunishClientToken punishClientToken = new PunishClientToken();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = Array.plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement("SELECT * FROM punishments WHERE punished_uuid = ?");
            ps.setString(1, targetUuid);
            rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            } else {
                punishClientToken.punishId = rs.getInt("punish_id");
                punishClientToken.appealKey = rs.getString("appeal_key");
                punishClientToken.punisherUuid = rs.getString("punisher_uuid");
                punishClientToken.punishType = rs.getInt("punish_type");
                punishClientToken.reason = rs.getString("reason");
                punishClientToken.punishedUuid = rs.getString("punished_uuid");
                punishClientToken.date = rs.getLong("date");
                punishClientToken.until = rs.getLong("until");
                punishClientToken.active = rs.getBoolean("active");
                punishClientToken.removed = rs.getBoolean("removed");
                punishClientToken.removeReason = rs.getString("remove_reason");
                punishClientToken.removeAdmin = rs.getString("remove_admin");
                return punishClientToken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.plugin.getDatabaseManager().closeComponents(rs, ps, connection);
        }
        return punishClientToken;
    }
}
