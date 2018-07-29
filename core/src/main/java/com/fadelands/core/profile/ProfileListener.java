package com.fadelands.core.profile;

import com.fadelands.core.achievement.AchievementGui;
import com.fadelands.core.profile.statistics.StatsListener;
import com.fadelands.core.utils.ItemBuilder;
import com.fadelands.array.Array;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("Duplicates")
public class ProfileListener implements Listener {


    private final static String inventoryName = "Profile";

    public static void openProfileInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, "§lYour " + inventoryName);

        inv.clear();

        inv.setItem(10, getSkull(player));
        inv.setItem(12, achievements());
        inv.setItem(14, statistics());
        inv.setItem(16, friends());

        inv.setItem(26, closeInv());

        player.openInventory(inv);
    }

    public static void openOtherPlayerProfileInv(Player cmdSender, String targetName) {
        String user = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

            try {
                connection = Array.getConnection();
                ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_username = ?");
                ps.setString(1, targetName);
                rs = ps.executeQuery();
                if (rs.next()) {
                    user = rs.getString("player_username");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                Array.closeComponents(rs, ps, connection);
            }

        Inventory inv = Bukkit.createInventory(null, 9 * 3, "§l" + user + "'s " + inventoryName);

        inv.clear();


        inv.setItem(10, getSkullTarget(user));
        inv.setItem(12, achievementsTarget(user));
        inv.setItem(14, statisticsTarget(user));
        inv.setItem(16, friendsTarget());

        inv.setItem(26, closeInv());

    }

    // THESE ARE FOR THE TARGET INVENTORY!
    private static ItemStack achievementsTarget(String name) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String target = null;

        try {
            connection = Array.getConnection();
            ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_username = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                target = rs.getString("player_username");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
            Array.closeComponents(rs, ps, connection);
        }


        return new ItemBuilder(Material.EXP_BOTTLE).setName("§3Achievements").setLore("§7Click to view " + target + "'s achievements.").toItemStack();

    }

    private static ItemStack statisticsTarget(String name) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String target = null;

        try {
            connection = Array.getConnection();
            ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_username = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                target = rs.getString("player_username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Array.closeComponents(rs, ps, connection);
        }

        return new ItemBuilder(Material.BOW).setName("§3Statistics").setLore("§7Click to view " + target + "'s server stats.").toItemStack();

    }

    private static ItemStack friendsTarget() {
        return new ItemBuilder(Material.SIGN).setName("§3Friends").setLore("§cCurrently in development.").toItemStack();

    }

    private static ItemStack getSkullTarget(String name) {
        String target = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

            try {
                connection = Array.getConnection();
                ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_username = ?");
                ps.setString(1, name);
                rs = ps.executeQuery();
                if (rs.next()) {
                     target = rs.getString("player_username");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                Array.closeComponents(rs, ps, connection);
            }

        try (Connection connection1 = Array.getConnection()) {
            try (PreparedStatement st = connection1.prepareStatement("SELECT * FROM `luckperms_players` WHERE `username`='" + target + "'")) {
                try (ResultSet rs1 = st.executeQuery()) {
                    if (rs1.next()) {
                        return new ItemBuilder(Material.SKULL_ITEM).setSkullOwner(target).setData(3).setName("§3About " + target).setLore("§7" + target + "'s Rank: §b" + rs1.getString("primary_group").toUpperCase()).toItemStack();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }



    // THESE ARE FOR THE "SELF" INVENTORY!
    private static ItemStack closeInv() {
            return new ItemBuilder(Material.BARRIER).setName("§cClose Profile").setLore("§7Close the profile menu.").toItemStack();

        }
    private static ItemStack achievements() {
        return new ItemBuilder(Material.EXP_BOTTLE).setName("§3Achievements").setLore("§7Click to view your achievements.").toItemStack();

    }
    private static ItemStack statistics() {
        return new ItemBuilder(Material.BOW).setName("§3Statistics").setLore("§7Click to view your server stats.").toItemStack();

    }
    private static ItemStack friends() {
        return new ItemBuilder(Material.SIGN).setName("§3Friends").setLore("§cCurrently in development.").toItemStack();

    }
    private static ItemStack getSkull(Player player) {

        try(Connection connection = Array.getConnection()){
            try(PreparedStatement st = connection.prepareStatement("SELECT * FROM `luckperms_players` WHERE `username`='" + player.getName() + "'")) {
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        return new ItemBuilder(Material.SKULL_ITEM).setSkullOwner(player.getName()).setData(3).setName("§3About You").setLore("§7Your Rank: §b" + rs.getString("primary_group").toUpperCase()).toItemStack();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    return null;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals("§lYour " + inventoryName)) {
            event.setCancelled(true);

            if (event.getCurrentItem().isSimilar(getSkull(player))) {
            }
            if (event.getCurrentItem().isSimilar(achievements())) {
                AchievementGui.openPersonalAchievements(player);
            }
            if (event.getCurrentItem().isSimilar(statistics())) {
                StatsListener.openStatsInv(player);
            }
            if (event.getCurrentItem().isSimilar(friends())) {
                player.sendMessage("§cWe are currently working on this feature. Come back later!");
            }
            if (event.getCurrentItem().isSimilar(closeInv())) {
            player.closeInventory();
            }
        }
    }
}




