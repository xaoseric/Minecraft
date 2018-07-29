package com.fadelands.array.database.logaction;

import com.fadelands.array.Array;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("ALL")

/**
   @since yesterday this isn't used anymore. :)
 **/

public class InsertLog {

    public InsertLog() {

    }

    public static void insertServerLog(String logAction, String logType, String server, String logMessage){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = Array.getConnection();
            ps = connection.prepareStatement("INSERT INTO fadelands_server_logs (time,server,type,action,logs) VALUES (?,?,?,?,?)");

            ps.setTimestamp(1, new java.sql.Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
            ps.setString(2, server);
            ps.setString(3, logType);
            ps.setString(4, logAction);
            ps.setString(5, logMessage);
            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            Array.closeComponents(rs, ps, connection);
        }

    }
    public static void insertPlayerLog(String logAction, String logType, String server, String logMessage){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = Array.getConnection();
            ps = connection.prepareStatement("INSERT INTO fadelands_players_logs (time,server,type,action,logs) VALUES (?,?,?,?,?)");

            ps.setTimestamp(1, new java.sql.Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
            ps.setString(2, server);
            ps.setString(3, logType);
            ps.setString(4, logAction);
            ps.setString(5, logMessage);
            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            Array.closeComponents(rs, ps, connection);
        }

    }

}
