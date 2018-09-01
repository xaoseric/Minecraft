package com.fadelands.core.commands.help.guides;

import com.fadelands.core.CorePlugin;
import com.fadelands.core.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuideMenu implements Listener {

    public CorePlugin plugin;

    public GuideMenu(CorePlugin plugin) {
        this.plugin = plugin;
    }

    private final static String invName = "§lGuides";

    private static ItemStack linkDiscord = new ItemBuilder(Material.BOOK).setName("§6Linking your Discord").setLore("§7Click to view!").setAmount(1).toItemStack();

    private static ItemStack back = new ItemBuilder(Material.BARRIER).setName("§cGo Back").setLore("§7Click to go back to the help page.").setAmount(1).toItemStack();

    public static void openInventory(Player player) {
        Inventory inv = Bukkit.getServer().createInventory(null, 9*3, invName);

        inv.clear();

        inv.setItem(0, linkDiscord);

        inv.setItem(26, back);

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(invName)) {
            event.setCancelled(true);

            if (event.getCurrentItem().isSimilar(back)) {
                player.performCommand("help");
            }
            if (event.getCurrentItem().isSimilar(linkDiscord)) {
                DiscordLinkGuide.openInventory(player);
            }
        }
    }
}