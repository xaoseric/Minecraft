package com.fadelands.core.commands.help.inventory;

import com.fadelands.core.Core;
import com.fadelands.core.utils.ItemBuilder;
import com.fadelands.core.utils.ServerStatistics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class ServerStatsInventory implements Listener {

    private Core corePlugin;
    private ServerStatistics serverStats;
    private final static String inventoryName = "Server Statistics";

    public ServerStatsInventory(Core core, ServerStatistics serverStats) {
        this.corePlugin = core;
        this.serverStats = serverStats;
    }

    public void openInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, inventoryName);

        inv.clear();

        updateInventory(inv);

        player.openInventory(inv);

    }

    private void updateInventory(Inventory inv) {
        inv.clear();

        inv.setItem(10, new ItemBuilder(Material.PAPER).setName("§6Unique Players").setLore(Arrays.asList("§7A total of §6" + serverStats.getUniquePlayers() + " §7unique players", "§7has joined FadeLands.")).toItemStack());
        inv.setItem(12, new ItemBuilder(Material.PAPER).setName("§6Chat Messages").setLore(Arrays.asList("§7A total of §6" + serverStats.getAmountChatMessages() + " §7chat messages", "§7has been sent on FadeLands.")).toItemStack());
        inv.setItem(14, new ItemBuilder(Material.PAPER).setName("§6Commands Executed").setLore(Arrays.asList("§7A total of §6" + serverStats.getCommandsUsed() + " §7commands", "§7has been used on FadeLands.")).toItemStack());
        inv.setItem(16, new ItemBuilder(Material.PAPER).setName("§6Punishments Issued").setLore(Arrays.asList("§7A total of §6" + serverStats.getIssuedPunishments() + " §7punishments", "§7has been issued on FadeLands.")).toItemStack());

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;
        if (event.getInventory().getName().equals(inventoryName)) {
            event.setCancelled(true);

        }
    }
}