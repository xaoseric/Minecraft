package com.fadelands.core.staff.data;

import com.fadelands.core.Core;
import com.fadelands.core.player.User;
import com.fadelands.core.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveStaffData implements Listener {

    public Core plugin;

    public SaveStaffData(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void disconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (User.isStaff(player.getName())) {

            for (StaffMemberData staffdata : StaffMemberData.getAll()) {
                try (PreparedStatement statement = plugin.getDatabaseManager().getConnection().prepareStatement("UPDATE players_staff SET " +
                        "bans=?,kicks=?,mutes=?,reports_handled=?" +
                        " WHERE player_uuid='" + staffdata.getUUID() + "'")){
                     statement.setInt(1, staffdata.getStats().getBans());
                     statement.setInt(2, staffdata.getStats().getKicks());
                     statement.setInt(3, staffdata.getStats().getMutes());
                     statement.setInt(4, staffdata.getStats().getServerReports());
                     statement.execute();

                     User.getOnlineAdmins().sendMessage(Utils.AdminAlert + "§cStaff data has been saved in the database.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}