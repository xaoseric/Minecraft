package com.fadelands.core.commands.help.inventory;

import com.fadelands.core.utils.ItemBuilder;
import com.fadelands.core.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ApplyGui implements Listener {

    public CorePlugin plugin;

    public ApplyGui(CorePlugin plugin) {
        this.plugin = plugin;
    }

    private final static String invName = "Applications";

    private static ItemStack staff = new ItemBuilder(Material.BOOK).setName("§6§lModerator").setLore("§7Become a staff member and contribute", "§7your time to moderate the server.").setAmount(1).toItemStack();
    private static ItemStack builder = new ItemBuilder(Material.DIAMOND_PICKAXE).setName("§a§lBuilder").setLore("§7Join the build team and contribute to","§7make amazing builds!").setAmount(1).toItemStack();
    private static ItemStack content_creator = new ItemBuilder(Material.NAME_TAG).setName("§d§lContent Creator").setLore("§7Want to gain some advantages for streaming or" ,"§7recording on our server? Click here","§7to read the requirements!").toItemStack();
    private static ItemStack jobs = new ItemBuilder(Material.REDSTONE_COMPARATOR).setName("§c§lJobs").setLore("§7Want to work for FadeLands as Developer?" ,"§7Click here to find out more!").setAmount(1).toItemStack();
    private static ItemStack back = new ItemBuilder(Material.BARRIER).setName("§cGo Back").setLore("§7Click to go back to the help page.").setAmount(1).toItemStack();


    static Inventory applyInv = Bukkit.getServer().createInventory(null, 27, invName);


    public static boolean openApplyInv(Player player) {
        applyInv.clear();

        applyInv.setItem(10, staff);
        applyInv.setItem(12, builder);
        applyInv.setItem(14, content_creator);
        applyInv.setItem(16, jobs);
        applyInv.setItem(22, back);

        player.openInventory(applyInv);

        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(invName)) {
            event.setCancelled(true);

            if (event.getCurrentItem().isSimilar(staff)) {
                player.sendMessage("§r \n" +
                        "§7Read more about our staff applications at: \n " +
                        "§6https://www.fadelands.com/mod \n " +
                        "§r ");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(builder)) {
                player.sendMessage("§r \n" +
                        "§7Find out more about our build team and how to apply at: \n " +
                        "§ahttps://www.fadelands.com/builder \n " +
                        "§r ");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(content_creator)) {
                player.sendMessage("§r \n" +
                        "§7Find out more about our content creator ranks: \n " +
                        "§dhttps://www.fadelands.com/content-creators \n " +
                        "§r ");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(jobs)) {
                player.sendMessage("§r \n" +
                        "§7Read more about our job openings and positions at: \n " +
                        "§chttps://www.fadelands.com/jobs \n " +
                        "§r ");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(back)) {
                player.performCommand("help");
            }

        }
    }
}
