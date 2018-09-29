package com.fadelands.core.commands.help;

import com.fadelands.core.CorePlugin;
import com.fadelands.core.commands.help.guides.GuideMenu;
import com.fadelands.core.profile.ProfileListener;
import com.fadelands.core.profile.statistics.StatsListener;
import com.fadelands.core.settings.inventory.SettingsInventory;
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
import java.util.Arrays;

public class HelpInventory implements Listener {

    private CorePlugin corePlugin;
    private final static String inventoryName = "Help Center";

    public HelpInventory(CorePlugin core){
        this.corePlugin = core;
    }

    public void openInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 4, inventoryName);

        inv.clear();

        updateInventory(inv, player);

        player.openInventory(inv);

    }

    private void updateInventory(Inventory inv, Player player) {
        inv.clear();

        inv.setItem(4, new ItemBuilder(Material.BEACON).setName("§6§lHelp Center").setLore(Arrays.asList("§r",
                "§2Welcome to the Help Center!", "§7If you're looking for quick help this is", "§7the place to go!", "§r",
                "§aBrowse around in this menu and find what", "§ayou're looking for!", "§r",
                "§7If you still need help, please contact", "§7a staff member and they will", "§7gladly help you out!")).toItemStack());

        inv.setItem(10, new ItemBuilder(Material.BOOK).setName("§6Tutorials & Guides").setLore(Arrays.asList(
                "§7Not entirely sure how to start off?", "§r", "§7Take a look at our guides and see", "§7if they can help you.")).toItemStack());

        inv.setItem(12, new ItemBuilder(Material.REDSTONE_COMPARATOR).setName("§6Settings and Preferences").setLore(Arrays.asList(
                "§7Do you want your public chat off?", "§7Is the scoreboard in the way?", "§r", "§7Select your preferences so it suits you!")).toItemStack());

        inv.setItem(14, new ItemBuilder(Material.SKULL_ITEM).setData(3).setSkullOwner(player.getName()).setName("§6Profile").setLore(Arrays.asList(
                "§7Want to see your achievements or", "§7statistics? It's all a click away.")).toItemStack());

        inv.setItem(16, new ItemBuilder(Material.BOOK_AND_QUILL).setName("§6Applications & Jobs").setLore(Arrays.asList(
                "§7Interested to apply for staff?", "§7Or maybe you want to join our Build team?", "§r", "§7Click to find out more about our positions.")).toItemStack());
    }

    private static ItemStack unique_players() {
        int uniquePlayers = 0;
        try (Connection connection = Array.getConnection()) {
            try (PreparedStatement st = connection.prepareStatement("SELECT COUNT(*) FROM fadelands_players")) {
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        uniquePlayers = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ItemBuilder(Material.BEACON).setName("§6Unique Players").setLore("§7A total of §e" + uniquePlayers + " §7unique player", "§7have joined FadeLands!").toItemStack();
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(inventoryName)) {
            event.setCancelled(true);

            switch (event.getSlot()) {

                case 10:
                    GuideMenu.openInventory(player);
                    break;

                case 12:
                    new SettingsInventory(corePlugin).openInventory(player);
                    break;

                case 14:
                    break;

                case 16:
                    ApplyGui.openApplyInv(player);
                    break;
            }
        }
    }
}