package com.fadelands.core.profile.statistics;

import com.fadelands.core.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChooseStatsMenu {
    private final static String invName = "§lChoose a Server";

    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 6, invName);
        inv.clear();

        player.openInventory(inv);
    }

    private static ItemStack GlobalStats() {
        return new ItemBuilder(Material.BEACON).setName("§b§lGlobal Statistics").setLore("§7Click to view your global statistics.").toItemStack();

    }
}
