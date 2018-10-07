package com.fadelands.core.profile;

import com.fadelands.array.player.User;
import com.fadelands.core.achievement.AchievementGui;
import com.fadelands.core.profile.statistics.StatsListener;
import com.fadelands.core.utils.ItemBuilder;
import com.fadelands.array.Array;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@SuppressWarnings("Duplicates")
public class ProfileListener implements Listener {

    //todo: revamp the entire fucking code

    public void openInventory(Player executor, String target) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, target);

        updateInventory(inv, executor, target);

        executor.openInventory(inv);
    }

    public void updateInventory(Inventory inv, Player executor, String target) {
        inv.clear();

        inv.setItem(4, new ItemBuilder(Material.SKULL_ITEM).setData(3).setSkullOwner(target).setName("§6" + target).setLore(Arrays.asList("" +
                "§7Rank: " + new User().getRank(target),
                "§7Network Level:§2 999",
                "§7Points:§2 999",
                "§7Tokens:§2 0")
        ).toItemStack());

        inv.setItem(11, new ItemBuilder(Material.BOOK).setName("§6Achievements").setLore("§7Click to view " + target + "'s achievements.").toItemStack());

        inv.setItem(13, new ItemBuilder(Material.BOOK).setName("§6Stat").setLore("§7Click to view " + target + "'s achievements.").toItemStack());



    }

}




