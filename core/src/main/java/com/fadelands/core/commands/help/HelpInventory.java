package com.fadelands.core.commands.help;

import com.fadelands.core.profile.ProfileListener;
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

public class HelpInventory implements Listener {
    private final static String inventoryName = "§lHow can I help?";

    public static void openHelpInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, inventoryName);

        inv.clear();


        inv.setItem(9, rules());
        inv.setItem(10, vote());
        inv.setItem(11, store());
        inv.setItem(12, statistics());
        inv.setItem(13, profile(player));
        inv.setItem(14, forums());
        inv.setItem(15, discord());
        inv.setItem(16, staffteam());
        inv.setItem(17, applications());

        inv.setItem(22, unique_players());

        player.openInventory(inv);
    }

    private static ItemStack rules() {
        return new ItemBuilder(Material.BOOK_AND_QUILL).setName("§9Server Rules").setLore("§7Click to view our server rules.").toItemStack();
    }

    private static ItemStack vote() {
        return new ItemBuilder(Material.DOUBLE_PLANT).setName("§3Vote").setLore(Arrays.asList("§7Click to vote for the server and", "§7receive some goods!")).toItemStack();
    }

    private static ItemStack store() {
        return new ItemBuilder(Material.ENDER_CHEST).setName("§9Store").setLore("§7Click to go to our server store where", "§7you can buy ranks and perks!").toItemStack();
    }

    private static ItemStack statistics() {
        return new ItemBuilder(Material.BOW).setName("§3Statistics").setLore("§7Click to see your personal", "§7server statistics.").toItemStack();
    }

    private static ItemStack profile(Player player) {
        return new ItemBuilder(Material.SKULL_ITEM).setData(3).setSkullOwner(player.getName()).setName("§9Profile").setLore("§7Click to view your profile.").toItemStack();
    }

    private static ItemStack forums() {
        return new ItemBuilder(Material.EMERALD).setName("§3Forums").setLore("§7Click to visit our forums.").toItemStack();
    }

    private static ItemStack discord() {
        return new ItemBuilder(Material.STAINED_CLAY).setData(9).setName("§9Discord").setLore("§7Click to receive our Discord invite link.").toItemStack();
    }

    private static ItemStack staffteam() {
        return new ItemBuilder(Material.SKULL_ITEM).setData(3).setName("§3The Team").setLore("§7Click to see all of our staff members.").toItemStack();
    }

    private static ItemStack applications() {
        return new ItemBuilder(Material.BOOK).setName("§9Applications").setLore("§7Interested in becoming staff? Click here.").toItemStack();
    }

    private static ItemStack unique_players() {
        int uniquePlayers = 0;
        try (Connection connection = Array.getConnection()) {
            try (PreparedStatement st = connection.prepareStatement("SELECT COUNT(*) FROM fadelands_players")) {
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        uniquePlayers = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ItemBuilder(Material.BEACON).setName("§3Unique Players").setLore("§7A total of §b" + uniquePlayers + " §7unique players", "§7have joined FadeLands!").toItemStack();
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(inventoryName)) {
            event.setCancelled(true);

            if (event.getCurrentItem().isSimilar(rules())) {
                player.sendMessage("§7You can read our rules at §bhttps://www.fadelands.com/rules/§7.");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(staffteam())) {
                player.sendMessage("§7You can find a list of our staff members at §bhttps://www.fadelands.com/staff-team/§7.");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(vote())) {
                player.sendMessage("§7You can vote for the server at §bhttps://www.fadelands.com/vote/§7.");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(store())) {
                player.sendMessage("§7You can visit our server store at §bhttps://store.fadelands.com§7.");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(statistics())) {
                StatsListener.openStatsInv(player);
            }
            if (event.getCurrentItem().isSimilar(discord())) {
                player.sendMessage("§7You can join our Discord with this link: §bdiscord.fadelands.com§7.");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(forums())) {
                player.sendMessage("§7Here's a fancy link for you: §bhttps://www.fadelands.com§7. (in all seriousness, that's the website link right there)");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(applications())) {
                ApplyGui.openApplyInv(player);
            }
            if (event.getCurrentItem().isSimilar(profile(player))) {
                ProfileListener.openProfileInv(player);
            }
        }
    }
}