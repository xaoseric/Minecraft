package com.fadelands.core.profile.inventory;

import com.fadelands.core.Core;
import com.fadelands.core.player.UserUtil;
import com.fadelands.core.utils.ItemBuilder;
import com.fadelands.core.utils.LPUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings("Duplicates")
public class ProfileInventory implements Listener {

    private Core plugin;

    public ProfileInventory(Core plugin) {
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

        UUID uuid = UUID.fromString(UserUtil.getUuid(target));

        LPUtils.getPrefix(uuid);

        inv.setItem(4, new ItemBuilder(Material.SKULL_ITEM).setData(3).setSkullOwner(target).setName(LPUtils.getPrefix(uuid)).setLore(Arrays.asList("" +
                "§7Rank:§2 " + UserUtil.getRank(target).toUpperCase(),
                "§7Network Level: " + plugin.getEconomyManager().getNetworkLevel(UserUtil.getUuid(target)),
                "§7Points: §a" + plugin.getEconomyManager().getPoints(UserUtil.getUuid(target)),
                "§7Tokens: §a" + plugin.getEconomyManager().getTokens(UserUtil.getUuid(target)))
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




