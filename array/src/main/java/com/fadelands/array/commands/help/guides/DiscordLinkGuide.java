package com.fadelands.array.commands.help.guides;

import com.fadelands.array.Array;
import com.fadelands.array.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DiscordLinkGuide implements Listener {

    public Array plugin;

    public DiscordLinkGuide(Array plugin) {
        this.plugin = plugin;
    }

    private final static String invName = "Linking your Discord Account";

    private static ItemStack step1 = new ItemBuilder(Material.PAPER).setName("§6§lStep 1").setLore("§7When joining our Discord with",
            "§2discord.fadelands.com §7you will receive a",
            "§7message from the FadeLands bot with your",
            "§7Discord ID. Copy it.").setAmount(1).toItemStack();

    private static ItemStack step2 = new ItemBuilder(Material.PAPER).setName("§6§lStep 2").setLore("§7Type the command §2/discord link <your id>",
            "§7in-game and wait for the FadeLands bot to",
            "§7message you once again with a security key.").setAmount(1).toItemStack();

    private static ItemStack step3 = new ItemBuilder(Material.PAPER).setName("§6§lStep 3").setLore("§7Copy the key, and use the command",
            "§2/discord verify <paste your key here> §7.").toItemStack();

    private static ItemStack step4 = new ItemBuilder(Material.PAPER).setName("§6§lStep 4").setLore("§7You should then have your rank given",
            "§7to you in our Discord!").setAmount(1).toItemStack();

    private static ItemStack if_not = new ItemBuilder(Material.LEATHER).setName("§c§lDid this not help you?").setLore("§7If this guide didn't help you,",
            "§7feel free to contact a staff member and",
            "§7ask them for help. :)").setAmount(1).toItemStack();

    private static ItemStack back = new ItemBuilder(Material.BARRIER).setName("§cGo Back").setLore("§7Click to go back to the guides page.").setAmount(1).toItemStack();

    public static void openInventory(Player player) {
        Inventory inv = Bukkit.getServer().createInventory(null, 9, invName);

        inv.clear();

        inv.setItem(0, step1);
        inv.setItem(1, step2);
        inv.setItem(2, step3);
        inv.setItem(3, step4);

        inv.setItem(7, if_not);
        inv.setItem(8, back);

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
                GuideMenu.openInventory(player);
            }
        }
    }
}