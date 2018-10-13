package com.fadelands.core.staff.inventory;

import com.fadelands.core.Core;
import com.fadelands.core.player.User;
import com.fadelands.core.utils.ItemBuilder;
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

public class StaffInventory implements Listener {

    private Core core;

    private static String invName = "Staff Overview";
    public StaffInventory(Core core) {
        this.core = core;
    }

    public void openInventory(Player player, String targetName){
        Inventory inv = Bukkit.createInventory(null, 9*3, invName);

        updateInventory(inv, targetName);

        player.openInventory(inv);
    }

    public void updateInventory(Inventory inventory, String targetName) {
        String uuid = User.getUuid(targetName);

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = core.getDatabaseManager().getConnection();
            ps = connection.prepareStatement("SELECT * FROM staff_members WHERE player_uuid = ?");
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            if (rs.next()) {
                inventory.setItem(4, new ItemBuilder(Material.SKULL_ITEM).setData(3)
                        .setSkullOwner(targetName)
                        .setName("§6" + targetName)
                        .setLore(Arrays.asList("§7Staff Overview of",
                                "§7" + targetName))
                        .toItemStack());

                Timestamp hiredDateTimestamp = new Timestamp(rs.getTimestamp("date_hired").getTime());

                java.util.Date hiredDate = new java.util.Date(hiredDateTimestamp.getTime());
                java.util.Date resignationDate = null;

                if (rs.getTimestamp("resignation_date") != null) {
                    Timestamp resignationDateTimestamp = new Timestamp(rs.getTimestamp("resignation_date").getTime());
                    resignationDate = new Date(resignationDateTimestamp.getTime());
                }

                inventory.setItem(11, new ItemBuilder(Material.PAPER)
                        .setName("§6Date Hired")
                        .setLore(Arrays.asList("§7The staff member was",
                                "§7hired §2" + hiredDate + "§7."))
                        .toItemStack());

                inventory.setItem(12, new ItemBuilder(Material.PAPER)
                        .setName("§6Resignation Date")
                        .setLore(resignationDate == null ? "§7The staff member has not resigned yet." : "§7The staff member resigned",
                                "§2" + resignationDate + "§7.").toItemStack());

                inventory.setItem(13, new ItemBuilder(Material.PAPER)
                        .setName("§6Bans")
                        .setLore(Arrays.asList("§7The staff member has",
                                "§2" + rs.getInt("bans") + "§7 bans."))
                        .toItemStack());

                inventory.setItem(14, new ItemBuilder(Material.PAPER)
                        .setName("§6Mutes")
                        .setLore(Arrays.asList("§7The staff member has",
                                "§2" + rs.getInt("mutes") + "§7 mutes."))
                        .toItemStack());

                inventory.setItem(15, new ItemBuilder(Material.PAPER)
                        .setName("§6Reports Handled")
                        .setLore(Arrays.asList("§7The staff member has",
                                "§7handled §2" + rs.getInt("reports_handled") + "§7 reports."))
                        .toItemStack());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            core.getDatabaseManager().closeComponents(rs, ps, connection);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null
                || (event.getCurrentItem() == null)
                || !event.getInventory().getName().equals(invName)) return;
        Player player = (Player) event.getWhoClicked();
        String table = "staff_settings";
        event.setCancelled(true);

    }
    }
