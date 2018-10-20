package com.fadelands.sbair.abilities.inventory;

import com.fadelands.core.utils.ItemBuilder;
import com.fadelands.sbair.Main;
import com.fadelands.sbair.abilities.Ability;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AbilitiesInventory implements Listener {

    private Main plugin;

    public AbilitiesInventory(Main plugin) {
        this.plugin = plugin;
    }

    private static String invName = "Ability";

    public void openInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9*3, invName);

        updateInventory(inv, player);

        player.openInventory(inv);
    }

    public void updateInventory(Inventory inv, Player player) {
        for (Ability.Abilities ignored : Ability.Abilities.values()) {
            ItemStack item = new ItemBuilder(Material.BOOK).toItemStack();
        }

    }


}
