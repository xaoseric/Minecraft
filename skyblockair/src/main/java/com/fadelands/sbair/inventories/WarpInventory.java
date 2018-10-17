package com.fadelands.sbair.inventories;

import com.fadelands.core.Core;
import com.fadelands.core.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WarpInventory implements Listener {

    private static String name = "SkyBlock Warps";

    public void openInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9*3, name);

        updateInventory(inv);

        player.openInventory(inv);
    }

    public void updateInventory(Inventory inv) {
        inv.setItem(3, new ItemBuilder(Material.SIGN).setName("§6Information Centre").setLore("§7Click to warp to the §eInfo Centre§7.").toItemStack());
        inv.setItem(5, new ItemBuilder(Material.GRASS).setName("§6Island").setLore("§7Click to warp home to your island").toItemStack());

        inv.setItem(11, new ItemBuilder(Material.CHEST).setName("§6Crates").setLore("§7Click to warp to the §eCrates§7.").toItemStack());
        inv.setItem(12, new ItemBuilder(Material.ENCHANTMENT_TABLE).setName("§6Enchanting").setLore("§7Click to warp to the §eEnchanting Area§7.").toItemStack());
        inv.setItem(13, new ItemBuilder(Material.MOB_SPAWNER).setName("§6Animal Farm").setLore("§7Click to warp to the §eAnimal Farm§7.").toItemStack());
        inv.setItem(14, new ItemBuilder(Material.EMERALD).setName("§6Shop").setLore("§7Click to warp to the §eShop§7.").toItemStack());
        inv.setItem(15, new ItemBuilder(Material.WATCH).setName("§6Spawn").setLore("§7Click to warp to the §eSpawn§7.").toItemStack());

        inv.setItem(21, new ItemBuilder(Material.MAP).hideAttributes().setName("§6Island Warps").setLore("§7Click to view all the §eIsland Warps§7.").toItemStack());
        inv.setItem(22, new ItemBuilder(Material.BOOK).setName("§6Tutorials").setLore("§7Click to warp to the §eTutorials§7.").toItemStack());
        inv.setItem(23, new ItemBuilder(Material.COMPASS).setName("§6Lobby").setLore("§7Click to warp to the §eLobby§7.").toItemStack());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getInventory().getName().equals(name)) return;

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        switch (event.getSlot()) {
            case 3:
                player.performCommand("warp info");
                break;
            case 5:
                player.performCommand("is home");
                break;
            case 11:
                player.performCommand("warp crates");
                break;
            case 12:
                player.performCommand("warp enchant");
                break;
            case 13:
                player.performCommand("warp animalfarm");
                break;
            case 14:
                player.performCommand("warp shop");
                break;
            case 15:
                player.performCommand("spawn");
                break;
            case 21:
                player.performCommand("is warp");
                break;
            case 22:
                player.performCommand("warp tutorials");
                break;
            case 23:
                player.closeInventory();
                Core.plugin.getPluginMessage().sendToServer(player, "LOBBY");
                break;
        }
    }
}
