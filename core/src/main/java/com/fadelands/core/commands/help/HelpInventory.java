package com.fadelands.core.commands.help;

import com.fadelands.core.CorePlugin;
import com.fadelands.core.commands.help.guides.GuideMenu;
import com.fadelands.core.profile.ProfileListener;
import com.fadelands.core.profile.statistics.StatsListener;
import com.fadelands.core.settings.inventory.SettingsInventory;
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

    private CorePlugin corePlugin;
    private final static String inventoryName = "§lHow can we help?";

    public HelpInventory(CorePlugin core){
        this.corePlugin = core;
    }

    public static void openHelpInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, inventoryName);

        inv.clear();

        inv.setItem(9, guides());
        inv.setItem(10, vote());
        inv.setItem(11, store());
        inv.setItem(12, settings());
        inv.setItem(13, profile(player));
        inv.setItem(14, forums());
        inv.setItem(15, discord());
        inv.setItem(16, staffteam());
        inv.setItem(17, applications());

        inv.setItem(4, unique_players());

        player.openInventory(inv);
    }

    private static ItemStack guides() {
        return new ItemBuilder(Material.BOOK_AND_QUILL).setName("§6Tutorials & Guides").setLore("§7Click to view our guides and tutorials.").toItemStack();
    }

    private static ItemStack vote() {
        return new ItemBuilder(Material.DOUBLE_PLANT).setName("§eVote").setLore(Arrays.asList("§7Click to vote for the server and", "§7receive some goods!")).toItemStack();
    }

    private static ItemStack store() {
        return new ItemBuilder(Material.ENDER_CHEST).setName("§6Store").setLore("§7Click to visit our server store where", "§7you can buy ranks and perks!").toItemStack();
    }

    private static ItemStack settings() {
        return new ItemBuilder(Material.BOW).setName("§eYour Settings").setLore("§7Click to view and", "§7change your settings.").toItemStack();
    }

    private static ItemStack profile(Player player) {
        return new ItemBuilder(Material.SKULL_ITEM).setData(3).setSkullOwner(player.getName()).setName("§6Profile").setLore("§7Click to view your profile.").toItemStack();
    }

    private static ItemStack forums() {
        return new ItemBuilder(Material.EMERALD).setName("§eForums").setLore("§7Click to receive the link to our forums.").toItemStack();
    }

    private static ItemStack discord() {
        return new ItemBuilder(Material.STAINED_CLAY).setData(9).setName("§6Discord").setLore("§7Click to receive our Discord invite link.").toItemStack();
    }

    private static ItemStack staffteam() {
        return new ItemBuilder(Material.SKULL_ITEM).setData(3).setName("§eThe Team").setLore("§7Click to see all of our staff members.").toItemStack();
    }

    private static ItemStack applications() {
        return new ItemBuilder(Material.BOOK).setName("§6Applications").setLore("§7Click to view our application forms.").toItemStack();
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
        return new ItemBuilder(Material.BEACON).setName("§6Unique Players").setLore("§7A total of §e" + uniquePlayers + " §7unique players", "§7have joined FadeLands!").toItemStack();
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(inventoryName)) {
            event.setCancelled(true);

            if (event.getCurrentItem().isSimilar(guides())) {
                player.sendMessage("§6Our guides in-game are limited. View our website for more guides.");
                GuideMenu.openInventory(player);
            }
            if (event.getCurrentItem().isSimilar(staffteam())) {
                player.sendMessage("§7You can find a list of our staff members at §2https://www.fadelands.com/staff-team/§7.");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(vote())) {
                player.performCommand("vote");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(store())) {
                player.sendMessage("§7You can visit our server store at §2https://store.fadelands.com§7.");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(settings())) {
                SettingsInventory inv = new SettingsInventory(corePlugin);
                inv.openInventory(player);
            }
            if (event.getCurrentItem().isSimilar(discord())) {
                player.sendMessage("§7You can join our Discord by visiting §2https://discord.fadelands.com§7.");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(forums())) {
                player.sendMessage("§7Here's a fancy link for you: §2https://www.fadelands.com§7.");
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