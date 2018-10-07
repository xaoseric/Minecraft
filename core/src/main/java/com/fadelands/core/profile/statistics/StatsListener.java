package com.fadelands.core.profile.statistics;

import com.fadelands.core.profile.ProfileListener;
import com.fadelands.core.utils.ItemBuilder;
import com.fadelands.array.Array;
import com.fadelands.core.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class StatsListener implements Listener {
    private final static String invName = "§lYour Statistics";

    public static void openStatsInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, invName);
        inv.clear();

        inv.setItem(0, socialStats());
        inv.setItem(1, getFirstJoined(player));
        inv.setItem(2, getUsedCommands(player));
        inv.setItem(3, getAmountMessages(player));
        inv.setItem(4, getLoginCount(player));

        inv.setItem(9, gameplayStats());
        inv.setItem(10, getKills(player));
        inv.setItem(11, getDeaths(player));
        inv.setItem(12, getBlocksPlaced(player));
        inv.setItem(13, getBlocksRemoved(player));

        inv.setItem(26, back());

        player.openInventory(inv);
    }

    private static ItemStack back() {
        return new ItemBuilder(Material.BARRIER).setName("§cGo Back").setLore("§7Go back to the previous page.").toItemStack();

    }

    private static ItemStack socialStats() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE).setData(11).setName("§3§lSOCIAL STATISTICS").toItemStack();

    }

    private static ItemStack gameplayStats() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE).setData(11).setName("§3§lGAMEPLAY STATISTICS").toItemStack();

    }

    private static ItemStack getAmountMessages(Player player) {
        return new ItemBuilder(Material.BOOK_AND_QUILL).setName("§3Messages Sent").setLore("§7You have sent a total of §b" + PlayerData.get(player.getUniqueId()).getStats().getMessagesSent() + "§7 messages.").toItemStack();
    }

    private static ItemStack getUsedCommands(Player player) {
        return new ItemBuilder(Material.COMMAND).setName("§3Commands Used").setLore("§7You have used a total of §b" + PlayerData.get(player.getUniqueId()).getStats().getCommandsUsed() + "§7 commands.").toItemStack();

    }

    private static ItemStack getLoginCount(Player player) {
        return new ItemBuilder(Material.IRON_DOOR).setName("§3Login Count").setLore("§7You have logged in §b" + PlayerData.get(player.getUniqueId()).getStats().getLoginCount() + "§7 times.").toItemStack();
    }

    private static ItemStack getBlocksPlaced(Player player) {
        return new ItemBuilder(Material.GRASS).setName("§3Placed Blocks").setLore("§7You have placed §b" + PlayerData.get(player.getUniqueId()).getStats().getBlocksPlaced() + "§7 blocks.").toItemStack();
    }

    private static ItemStack getBlocksRemoved(Player player) {
        return new ItemBuilder(Material.BEDROCK).setName("§3Blocks Broken").setLore("§7You have broken §b" + PlayerData.get(player.getUniqueId()).getStats().getBlocksRemoved() + "§7 blocks.").toItemStack();
    }

    private static ItemStack getDeaths(Player player) {
        return new ItemBuilder(Material.SKULL_ITEM).setData(1).setName("§3Deaths").setLore("§7You have died §b" + PlayerData.get(player.getUniqueId()).getStats().getDeaths() + "§7 times.").toItemStack();
    }

    private static ItemStack getKills(Player player) {
        return new ItemBuilder(Material.DIAMOND_SWORD).setName("§3Murderers").setLore("§7You have slain §b" + PlayerData.get(player.getUniqueId()).getStats().getKills() + "§7 player.").toItemStack();
    }

    private static ItemStack getFirstJoined(Player player) {
        try(Connection connection = Array.getConnection()){
            try(PreparedStatement st = connection.prepareStatement("SELECT * FROM `fadelands_players` WHERE `player_username`='" + player.getName() + "'")) {
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Timestamp firstStamp = new Timestamp(rs.getTimestamp("first_join").getTime());
                        Date first = new Date(firstStamp.getTime());

                        return new ItemBuilder(Material.EMPTY_MAP).setName("§3First Joined").setLore("§7You first joined §bFadeLands§7 on §b" + f.format(first) + "§7.").toItemStack();
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

        if (event.getInventory().getName().equals(invName)) {
            event.setCancelled(true);

            if (event.getCurrentItem().isSimilar(back())) {
                ProfileListener.openProfileInv(player);

            }
        }
    }
}