package com.fadelands.essentials.commands.admin.inventory;

import com.fadelands.array.Array;
import com.fadelands.array.player.User;
import com.fadelands.array.utils.ItemBuilder;
import com.fadelands.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.sql.*;
import java.util.Arrays;
import java.util.Date;

public class WhoisInventory implements Listener {

    private Main plugin;

    public WhoisInventory(Main plugin) {
        this.plugin = plugin;
    }

    public void whoIs(Player commandSender, String playerName) {
        Inventory inventory = Bukkit.createInventory(null, 9*4, "WhoIs");

        updateInventory(inventory, playerName);

        commandSender.openInventory(inventory);
    }

    public void updateInventory(Inventory inventory, String targetName) {
        String uuid = new User().getUuid(targetName);

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = plugin.getInstance().getDatabaseManager().getConnection();
            ps = connection.prepareStatement("SELECT * FROM players WHERE player_uuid = ?");
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            if (rs.next()) {

                inventory.setItem(4, new ItemBuilder(Material.SKULL_ITEM).setData(3)
                        .setSkullOwner(targetName)
                        .setName("§6" + targetName)
                        .setLore(Arrays.asList("§7Information about",
                                "§7" + targetName))
                        .toItemStack());

                Timestamp firstJoinRaw = new Timestamp(rs.getTimestamp("first_join").getTime());

                java.util.Date firstJoin = new java.util.Date(firstJoinRaw.getTime());
                java.util.Date lastLogin = null;

                if (rs.getTimestamp("last_login") != null) {
                    Timestamp lastLoginRaw = new Timestamp(rs.getTimestamp("last_login").getTime());
                    lastLogin = new Date(lastLoginRaw.getTime());
                }

                inventory.setItem(11, new ItemBuilder(Material.PAPER)
                        .setName("§6§lFirst Join")
                        .setLore(Arrays.asList("§7User first joined",
                                "§2" + firstJoin + "§7."))
                        .toItemStack());

                inventory.setItem(12, new ItemBuilder(Material.PAPER)
                        .setName("§6§lLast Seen")
                        .setLore(Arrays.asList("§7User was last seen",
                                "§2" + (lastLogin == null ? "§cNo last login" : lastLogin) + "§7."))
                        .toItemStack());

                inventory.setItem(13, new ItemBuilder(Material.PAPER)
                        .setName("§6§lIP Address")
                        .setLore(Arrays.asList("§7Users IP address is",
                                "§2" + new User().getIp(targetName) + "§7."))
                        .toItemStack());

                inventory.setItem(14, new ItemBuilder(Material.PAPER)
                        .setName("§6§lCountry")
                        .setLore(Arrays.asList("§7User is from",
                                "§2" + rs.getString("last_country") + "§7."))
                        .toItemStack());

                inventory.setItem(15, new ItemBuilder(Material.PAPER)
                        .setName("§6§lTimes Reported")
                        .setLore(Arrays.asList("§7User has been reported",
                                "§2" + rs.getInt("times_reported") + "§7 times."))
                        .toItemStack());

                inventory.setItem(21, new ItemBuilder(Material.PAPER)
                        .setName("§6§lRank")
                        .setLore(Arrays.asList("§7User has group",
                                "§2" + new User().getRank(targetName).toUpperCase() + "§7."))
                        .toItemStack());

                inventory.setItem(22, new ItemBuilder(Material.PAPER)
                        .setName("§6§lUUID")
                        .setLore(Arrays.asList("§7Users UUID is",
                                "§2" + new User().getUuid(targetName) + "§7."))
                        .toItemStack());

                inventory.setItem(23, new ItemBuilder(Material.PAPER)
                        .setName("§6§lLast Server")
                        .setLore("§2" + new User().getLastServer(targetName)).toItemStack());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            plugin.getInstance().getDatabaseManager().closeComponents(rs, ps, connection);
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null
                || (event.getCurrentItem() == null)
                || !event.getInventory().getName().equals("WhoIs")) return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);
    }

}
