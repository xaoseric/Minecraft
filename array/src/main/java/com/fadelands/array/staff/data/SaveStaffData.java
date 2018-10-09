package com.fadelands.array.staff.data;

import com.fadelands.array.player.User;
import com.fadelands.array.utils.Utils;
import com.fadelands.array.Array;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import sun.applet.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveStaffData implements Listener {

    public Main plugin;

    public SaveStaffData(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void disconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = new User();
        if (user.isStaff(player.getName())) {

            for (StaffMemberData staffdata : StaffMemberData.getAll()) {
                try (PreparedStatement statement = Array.getConnection().prepareStatement("UPDATE players_staff SET " +
                        "bans=?,kicks=?,mutes=?,reports_handled=?" +
                        " WHERE player_uuid='" + staffdata.getUUID() + "'")){
                     statement.setInt(1, staffdata.getStats().getBans());
                     statement.setInt(2, staffdata.getStats().getKicks());
                     statement.setInt(3, staffdata.getStats().getMutes());
                     statement.setInt(4, staffdata.getStats().getServerReports());
                     statement.execute();

                     new User().getOnlineAdmins().sendMessage(Utils.AdminAlert + "§cStaff data has been saved in the database.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}