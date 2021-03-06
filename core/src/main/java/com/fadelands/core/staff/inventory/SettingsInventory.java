package com.fadelands.core.staff.inventory;

import com.fadelands.core.Core;
import com.fadelands.core.staff.StaffSettings;
import com.fadelands.core.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class SettingsInventory implements Listener {

    private Core core;
    private StaffSettings staffSettings;

    private static String invName = "Staff Settings";
    public SettingsInventory(Core core) {
        this.core = core;
        this.staffSettings = new StaffSettings();
    }

    public void openInventory(Player player){
        Inventory inv = Bukkit.createInventory(null, 9*3, invName);

        updateInventory(inv, player);

        player.openInventory(inv);
    }

    public void updateInventory(Inventory inventory, Player player) {
         inventory.setItem(10, new ItemBuilder(Material.POTION).setName("§6Vanish")
                .setLore(Arrays.asList("§r",
                        "§7Current State: " + (!staffSettings.vanishOn(player) ? "§cDisabled" : "§aEnabled"),
                        "§r", "§eClick to turn vanish " + (staffSettings.vanishOn(player) ? "§coff" : "§aon") + "§e.")).toItemStack());
        inventory.setItem(13, new ItemBuilder(Material.WATCH).setName("§6Staff Chat")
                .setLore(Arrays.asList("§r",
                        "§7Current State: " + (!staffSettings.staffChatOn(player) ? "§cDisabled" : "§aEnabled"),
                        "§r", "§eClick to turn staff chat " + (staffSettings.staffChatOn(player) ? "§coff" : "§aon") + "§e.")).toItemStack());
        inventory.setItem(16, new ItemBuilder(Material.WATCH).setName("§6Admin Notifications")
                .setLore(Arrays.asList("§r",
                        "§7Current State: " + (!staffSettings.adminNotificationsOn(player) ? "§cDisabled" : "§aEnabled"),
                        "§r", "§eClick to turn admin notifications " + (staffSettings.adminNotificationsOn(player) ? "§coff" : "§aon") + "§e.")).toItemStack());
        inventory.setItem(4, new ItemBuilder(Material.ARROW).setName("§cExit")
                .setLore("§7Click to exit.").toItemStack());
    }

    @EventHandler
    public void onGameInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null
                || (event.getCurrentItem() == null)
                || !event.getInventory().getName().equals(invName)) return;
        Player player = (Player) event.getWhoClicked();
        String table = "staff_settings";
        event.setCancelled(true);

        switch (event.getSlot()) {
            case 10:
                boolean vanish = staffSettings.vanishOn(player);
                vanish = !vanish;
                player.performCommand("vanish");
                core.getDatabaseManager().updateTable(player, table, "vanish_toggled", vanish);
                updateInventory(event.getInventory(), player);
                break;
            case 13:
                boolean staffchat = staffSettings.staffChatOn(player);
                staffchat = !staffchat;

                core.getDatabaseManager().updateTable(player, table, "staff_chat", staffchat);
                updateInventory(event.getInventory(), player);
                break;
            case 16:
                boolean notifications = staffSettings.adminNotificationsOn(player);
                notifications = !notifications;

                core.getDatabaseManager().updateTable(player, table, "admin_notifications", notifications);
                updateInventory(event.getInventory(), player);
                break;
            case 4:
                openInventory(player);
                break;
        }
    }
}
