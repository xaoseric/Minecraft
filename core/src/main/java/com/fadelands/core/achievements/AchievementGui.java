package com.fadelands.core.achievements;

import com.fadelands.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AchievementGui implements Listener {
    private final static String inventoryName = "Achievements";

    public static void openPersonalAchievements(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 6, "Your " + inventoryName);
        inv.clear();
        player.openInventory(inv);

        }


    public static void openOtherPlayerAchievements(Player player, String target) {
        try(Connection connection = Core.plugin.getDatabaseManager().getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE player_username='" + target + "'")) {
                try (ResultSet rs = statement.executeQuery()) {
                    if (!rs.next()) {
                        player.sendMessage("§cAn error occurred while loading " + target + "'s achievements.");
                    } else {
                        Inventory inv = Bukkit.createInventory(null, 9 * 6, "" + rs.getString("player_username") + "'s " + inventoryName);
                        inv.clear();
                        player.openInventory(inv);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
