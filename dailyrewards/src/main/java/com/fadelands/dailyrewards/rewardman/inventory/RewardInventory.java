package com.fadelands.dailyrewards.rewardman.inventory;

import com.fadelands.array.utils.ItemBuilder;
import com.fadelands.dailyrewards.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class RewardInventory implements Listener {

    private Main plugin;
    private static String NAME = "§lYour Rewards";

    public RewardInventory(Main plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9*5, NAME);

        updateInventory(inv, player);

        player.openInventory(inv);
    }

    public void updateInventory(Inventory inv, Player player) {

        ItemStack barrier = new ItemBuilder(Material.STAINED_GLASS_PANE).setData(7).setName("§r").toItemStack();
        //first row
        inv.setItem(0, barrier);
        inv.setItem(1, barrier);
        inv.setItem(2, barrier);
        inv.setItem(3, barrier);
        inv.setItem(4, barrier);
        inv.setItem(5, barrier);
        inv.setItem(6, barrier);
        inv.setItem(7, barrier);
        inv.setItem(8, barrier);
        // second row
        inv.setItem(9, barrier);
        inv.setItem(10, barrier);
        inv.setItem(11, barrier);
        inv.setItem(12, barrier);
        inv.setItem(13, new ItemBuilder(Material.JUKEBOX).setName("§a§lVote Reward").setLore(Arrays.asList("§r", "§fClick to claim!", "§r",
                "§7Vote for FadeLands and receive", "§7great goods in return!", "§r", "§6Times Voted: §70",
                "§6Vote Streak: §70", "§6Highest Streak: §70",
                "§r")).toItemStack());
        inv.setItem(14, barrier);
        inv.setItem(15, barrier);
        inv.setItem(16, barrier);
        inv.setItem(17, barrier);
        // third row
        inv.setItem(18, barrier);
        inv.setItem(19, barrier);
        inv.setItem(20, new ItemBuilder(Material.STAINED_CLAY).setData(9).setName("§a§lDiscord").setLore(Arrays.asList("§r", "§fClick to claim!",
                "§r", "§7Join our Discord and get a reward in return!", "§r")).toItemStack());
        inv.setItem(21, barrier);
        inv.setItem(22, barrier);
        inv.setItem(23, barrier);
        inv.setItem(24, new ItemBuilder(Material.EXP_BOTTLE).setName("§c§lDaily Reward").setLore(Arrays.asList("§r", "§fThis reward can be claimed",
                "§fagain in §c24 hours§f.",
                "§r", "§7Come back every day and claim",
                "§7your daily reward!",
                "§r", "§6Claim Streak: §70", "§6Highest Streak: §70", "§r")).toItemStack());
        inv.setItem(25, barrier);
        inv.setItem(26, barrier);
        // fourth row
        inv.setItem(27, barrier);
        inv.setItem(28, barrier);
        inv.setItem(29, barrier);
        inv.setItem(30, barrier);
        inv.setItem(31, barrier);
        inv.setItem(32, barrier);
        inv.setItem(33, barrier);
        inv.setItem(34, barrier);
        inv.setItem(35, barrier);
        // fifth row
        inv.setItem(36, barrier);
        inv.setItem(37, barrier);
        inv.setItem(38, barrier);
        inv.setItem(39, barrier);
        inv.setItem(40, barrier);
        inv.setItem(41, barrier);
        inv.setItem(42, barrier);
        inv.setItem(43, barrier);
        inv.setItem(44, barrier);


    }

}
