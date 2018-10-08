package com.fadelands.core.profile.inventory;

import com.fadelands.array.Array;
import com.fadelands.array.player.User;
import com.fadelands.core.CorePlugin;
import com.fadelands.core.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

@SuppressWarnings("Duplicates")
public class ProfileInventory implements Listener {

    private CorePlugin plugin;

    public ProfileInventory(CorePlugin plugin) {
        this.plugin = plugin;
    }

    //todo: revamp the entire fucking code

    public static String ProfileInv = "Profile";
    public static String AchievementsInv = "Achievements";
    public static String StatsInv = "Statistics";

    public void openProfileInventory(Player executor, String target) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, ProfileInv);

        updateProfileInventory(inv, executor, target);

        executor.openInventory(inv);
    }

    public void updateProfileInventory(Inventory inv, Player executor, String target) {
        inv.clear();

        World world = Bukkit.getWorld("world");
        inv.setItem(4, new ItemBuilder(Material.SKULL_ITEM).setData(3).setSkullOwner(target).setName("§6" + target).setLore(Arrays.asList("" +
                "§7Rank:§2 " + new User().getRank(target).toUpperCase(),
                "§7Network Level:§2 999",
                "§7Points:§2 999",
                "§7Tokens:§2 0")
        ).toItemStack());

        inv.setItem(11, new ItemBuilder(Material.BOOK).setName("§6Achievements").setLore("§7Click to view " + target + "'s achievements.").toItemStack());

        inv.setItem(15, new ItemBuilder(Material.BOW).setName("§6Statistics").setLore("§7Click to view " + target + "'s statistics.").toItemStack());

    }

    @EventHandler
    public void onProfileInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(ProfileInv)) {
            event.setCancelled(true);
            switch (event.getSlot()) {
                case 11:
                    break;
                case 15:
                    break;
            }
        }
    }

    public void openStatsInventory(Player executor, String target) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, ProfileInv);

        updateStatsInventory(inv, executor, target);

        executor.openInventory(inv);
    }

    public void updateStatsInventory(Inventory inv, Player executor, String target) {
        inv.clear();

    }

    @EventHandler
    public void onStatsInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(StatsInv)) {
            event.setCancelled(true);
            switch (event.getSlot()) {
                case 11:
                    break;
                case 15:
                    break;
            }
        }
    }
}




