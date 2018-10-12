package com.fadelands.core.staff.data;

import com.fadelands.core.Core;
import com.fadelands.core.player.User;
import com.fadelands.core.utils.Utils;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class LoadStaffData {


    private static void loadStaffData(Player player) {
        Core.plugin.getDatabaseManager().executeQuery("SELECT * FROM `players_staff` WHERE `player_uuid`='" + player.getUniqueId().toString() + "'", resultPlayerStats -> {
                try {
                    if (resultPlayerStats.next()) {
                        User user = new User();
                        if (user.isStaff(player.getName())) {
                            StaffMemberData.ModStats stats = new StaffMemberData(player.getUniqueId()).getStats();
                            stats.setBans(resultPlayerStats.getInt("bans"));
                            stats.setKicks(resultPlayerStats.getInt("kicks"));
                            stats.setMutes(resultPlayerStats.getInt("mutes"));
                            stats.setServerReports(resultPlayerStats.getInt("reports_handled"));
                            resultPlayerStats.close();

                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new User().getOnlineAdmins().sendMessage(Utils.AdminAlert + "§cCouldn't load " + player.getName() + "'s staff data.");
                }
            });

        }

    }


