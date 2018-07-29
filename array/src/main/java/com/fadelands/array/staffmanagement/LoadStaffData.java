package com.fadelands.array.staffmanagement;

import com.fadelands.array.Array;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class LoadStaffData {


    private static void loadStaffData(Player player) {
        Array.executeQuery("SELECT * FROM `fadelands_players_staff` WHERE `player_uuid`='" + player.getUniqueId().toString() + "'", resultPlayerStats -> {

                try {
                    if (resultPlayerStats.next()) {
                        if (player.hasPermission("fadelands.staffdata")) {

                            StaffMemberData.ModStats stats = new StaffMemberData(player.getUniqueId()).getStats();
                            stats.setBans(resultPlayerStats.getInt("bans"));
                            stats.setKicks(resultPlayerStats.getInt("kicks"));
                            stats.setMutes(resultPlayerStats.getInt("mutes"));
                            stats.setServerReports(resultPlayerStats.getInt("reports_handled"));
                            player.sendMessage("§2✔ §7Staff data loaded successfully.");
                            resultPlayerStats.close();

                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    player.sendMessage("§4✖ §cCouldn't load your staff data. If this issues persists, report it to a staff member.");

                }
            });

        }

    }


