/*

MAKE THIS A SEPARATE PLUGIN.

package com.fadelands.core.npcs.dailycrates;

import com.fadelands.core.CorePlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class DailySpin {

    private static Plugin plugin;
    public DailySpin(CorePlugin plugin) {
        this.plugin = plugin;
    }

    public static void openDailySpin(Player player) {

        ItemStack[] items = new ItemStack[7];
        items[0] = new ItemStack(Material.BAKED_POTATO);
        items[1] = new ItemStack(Material.APPLE);
        items[2] = new ItemStack(Material.GOLD_AXE);
        items[3] = new ItemStack(Material.DIAMOND);
        items[4] = new ItemStack(Material.DIRT);
        items[5] = new ItemStack(Material.BOOKSHELF);
//This is all the rewards the player can get

        CrateInventory crate = new CrateInventory(9, items, "The name", plugin);
//This is a crate. It has 9 slots, has the "items" as the rewards, and will have the title "The Name".
//The "this" represents the main class's instance. If you are creating this crate in another class besides the main one,
//you will need to replace "this" with an instance of the main class.
        crate.spin(8, player);
    }

}
*/