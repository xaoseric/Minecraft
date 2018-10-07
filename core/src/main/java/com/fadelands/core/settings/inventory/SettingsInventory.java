package com.fadelands.core.settings.inventory;

import com.fadelands.array.database.SQLUtils;
import com.fadelands.array.utils.Utils;
import com.fadelands.core.CorePlugin;
import com.fadelands.core.settings.Settings;
import com.fadelands.core.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class SettingsInventory implements Listener {

    private CorePlugin core;
    private Settings settings;

    private static String MAIN = "Your Settings";
    private static String SOCIAL = "Your Social Settings";
    private static String GAME = "Your Game Settings";
    private static String LOBBY = "Your Lobby Settings";

    public SettingsInventory(CorePlugin core) {
        this.core = core;
        this.settings = new Settings();
    }

    public void openInventory(Player player){
        Inventory inv = Bukkit.createInventory(null, 9*3, MAIN);

        inv.setItem(10, new ItemBuilder(Material.RED_ROSE).setName("§6Social Settings").setLore("§7Click to view your social settings.").toItemStack());
        inv.setItem(13, new ItemBuilder(Material.MAP).setName("§6Game Settings").setLore("§7Click to view your game settings.").toItemStack());
        inv.setItem(16, new ItemBuilder(Material.REDSTONE_COMPARATOR).setName("§6Lobby Settings").setLore("§7Click to view your lobby settings.").toItemStack());
        player.openInventory(inv);
    }

    public void openSocialSettings(Player player){
        Inventory inv = Bukkit.createInventory(null, 9*5, SOCIAL);

        updateSocialInventory(inv, player);
        player.openInventory(inv);
    }

    public void openGameSettings(Player player){
        Inventory inv = Bukkit.createInventory(null, 9*3, GAME);

        updateGameInventory(inv, player);
        player.openInventory(inv);
    }

    public void openLobbySettings(Player player){
        Inventory inv = Bukkit.createInventory(null, 9*3, LOBBY);

        updateLobbyInventory(inv, player);
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null
                || (event.getCurrentItem() == null)
                || !event.getInventory().getName().equals(MAIN)) return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        switch (event.getSlot()) {
            case 10:
                openSocialSettings(player);
                break;
            case 13:
                openGameSettings(player);
                break;
            case 16:
                openLobbySettings(player);
                break;
        }
    }

    @EventHandler
    public void onSocialInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null
                || (event.getCurrentItem() == null)
                || !event.getInventory().getName().equals(SOCIAL)) return;
        Player player = (Player) event.getWhoClicked();
        String table = "fadelands_players_settings";
        event.setCancelled(true);

        switch (event.getSlot()) {
            case 10:
                boolean friendRequests = settings.allowingFriendRequests(player);
                friendRequests = !friendRequests;

                SQLUtils.updateTable(player, table, "friend_requests", friendRequests);
                updateSocialInventory(event.getInventory(), player);
                break;
            case 12:
                boolean publicChat = settings.publicChat(player);
                publicChat = !publicChat;

                SQLUtils.updateTable(player, table, "public_chat", publicChat);
                updateSocialInventory(event.getInventory(), player);
                break;
            case 14:
                boolean privateMessages = settings.allowingPrivateMessages(player);
                privateMessages = !privateMessages;

                SQLUtils.updateTable(player, table, "private_messages", privateMessages);
                updateSocialInventory(event.getInventory(), player);
                break;
            case 16:
                boolean informIfMuted = settings.informIfMuted(player);
                informIfMuted = !informIfMuted;

                SQLUtils.updateTable(player, table, "inform_if_muted", informIfMuted);
                updateSocialInventory(event.getInventory(), player);
                break;
            case 29:
                boolean friendAlerts = settings.showFriendAlerts(player);
                friendAlerts = !friendAlerts;

                SQLUtils.updateTable(player, table, "show_friend_alerts", friendAlerts);
                updateSocialInventory(event.getInventory(), player);
                break;
            case 31:
                boolean friendJoinAlerts = settings.showFriendJoinAlerts(player);
                friendJoinAlerts = !friendJoinAlerts;

                SQLUtils.updateTable(player, table, "show_friend_join_alerts", friendJoinAlerts);
                updateSocialInventory(event.getInventory(), player);
                break;
            case 33:
                boolean partyInvites = settings.allowPartyInvites(player);
                partyInvites = !partyInvites;

                SQLUtils.updateTable(player, table, "party_requests", partyInvites);
                updateSocialInventory(event.getInventory(), player);
                break;
            case 4:
                openInventory(player);
                break;
                }
            }

    @EventHandler
    public void onLobbyInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null
                || (event.getCurrentItem() == null)
                || !event.getInventory().getName().equals(LOBBY)) return;
        Player player = (Player) event.getWhoClicked();
        String table = "fadelands_players_lobbysettings";
        event.setCancelled(true);

        switch (event.getSlot()) {
            case 11:
                boolean doubleJump = settings.lobbyDoubleJump(player);
                doubleJump = !doubleJump;

                SQLUtils.updateTable(player, table, "double_jump", doubleJump);
                updateLobbyInventory(event.getInventory(), player);
                break;
            case 15:
                boolean playerVisibility = settings.lobbyPlayerVisibility(player);
                playerVisibility = !playerVisibility;

                SQLUtils.updateTable(player, table, "player_visibility", playerVisibility);
                updateLobbyInventory(event.getInventory(), player);
                break;
            case 4:
                openInventory(player);
                break;
        }
    }

    @EventHandler
    public void onGameInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null
                || (event.getCurrentItem() == null)
                || !event.getInventory().getName().equals(GAME)) return;
        Player player = (Player) event.getWhoClicked();
        String table = "fadelands_players_settings";
        event.setCancelled(true);

        switch (event.getSlot()) {
            case 10:
                boolean gameTips = settings.allowingGameTips(player);
                gameTips = !gameTips;

                SQLUtils.updateTable(player, table, "game_tips", gameTips);
                updateGameInventory(event.getInventory(), player);
                break;
            case 13:
                boolean showScoreboard = settings.showScoreboard(player);
                showScoreboard = !showScoreboard;

                SQLUtils.updateTable(player, table, "show_scoreboard", showScoreboard);
                updateGameInventory(event.getInventory(), player);
                break;
            case 16:
                boolean showAnnouncements = settings.showAnnouncements(player);
                showAnnouncements = !showAnnouncements;

                SQLUtils.updateTable(player, table, "show_announcements", showAnnouncements);
                updateGameInventory(event.getInventory(), player);
                break;
            case 4:
                openInventory(player);
                break;
        }
    }

    private void updateSocialInventory(Inventory inventory, Player player){
        inventory.setItem(10, new ItemBuilder(Material.RED_ROSE).setName("§6Friend Requests")
                .setLore(Arrays.asList("§r",
                        "§7Current State: " + (!settings.allowingFriendRequests(player) ? "§cDisabled" : "§aEnabled"),
                        "§r", Utils.S + "Click to switch friend request privacy " + (settings.allowingFriendRequests(player) ? "§coff" : "§aon") + Utils.S + ".")).toItemStack());
        inventory.setItem(12, new ItemBuilder(Material.PAPER).setName("§6Public Chat")
                .setLore(Arrays.asList("§r",
                        "§7Current State: " + (!settings.publicChat(player) ? "§cDisabled" : "§aEnabled"),
                        "§r", Utils.S + "Click to toggle " + (settings.publicChat(player) ? "§coff" : "§aon") + Utils.S + " the public chat.")).toItemStack());
        inventory.setItem(14, new ItemBuilder(Material.BOOK_AND_QUILL).setName("§6Private Messages")
                .setLore(Arrays.asList("§r",
                        "§7Current State: " + (!settings.allowingPrivateMessages(player) ? "§cDisabled" : "§aEnabled"),
                        "§r", Utils.S + "Click to switch message privacy " + (settings.allowingPrivateMessages(player) ? "§coff" : "§aon") + Utils.S + ".")).toItemStack());
        inventory.setItem(16, new ItemBuilder(Material.BARRIER).setName("§6Inform If Muted")
                .setLore(Arrays.asList("§r",
                        "§7Current State: " + (!settings.informIfMuted(player) ? "§cDisabled" : "§aEnabled"),
                        "§r", Utils.S + "Click to toggle " + (settings.informIfMuted(player) ? "§coff" : "§aon") + Utils.S + " inform if muted.")).toItemStack());
        inventory.setItem(29, new ItemBuilder(Material.SKULL_ITEM).setData(3).setName("§6Show Friend Alerts")
                .setLore(Arrays.asList("§r",
                        "§7Current State: " + (!settings.showFriendAlerts(player) ? "§cDisabled" : "§aEnabled"),
                        "§r", Utils.S + "Click to toggle " + (settings.showFriendAlerts(player) ? "§coff" : "§aon") + Utils.S + " friend alerts.")).toItemStack());
        inventory.setItem(31, new ItemBuilder(Material.SKULL_ITEM).setData(3).setName("§6Show Friend Join/Quit Alerts")
                .setLore(Arrays.asList("§r",
                        "§7Current State: " + (!settings.showFriendJoinAlerts(player) ? "§cDisabled" : "§aEnabled"),
                        "§r", Utils.S + "Click to toggle " + (settings.showFriendJoinAlerts(player) ? "§coff" : "§aon") + Utils.S + " friend join/quit alerts.")).toItemStack());
        inventory.setItem(33, new ItemBuilder(Material.CHEST).setName("§6Party Invites")
                .setLore(Arrays.asList("§r",
                        "§7Current State: " + (!settings.allowPartyInvites(player) ? "§cDisabled" : "§aEnabled"),
                        "§r", Utils.S + "Click to switch party invite privacy " + (settings.allowPartyInvites(player) ? "§coff" : "§aon") + Utils.S + ".")).toItemStack());
        inventory.setItem(4, new ItemBuilder(Material.ARROW).setName("§cGo Back")
                .setLore("§7Click to go back.").toItemStack());

        }

        private void updateGameInventory(Inventory inventory, Player player) {
            inventory.setItem(10, new ItemBuilder(Material.DIAMOND_BOOTS).setName("§6Show Game Tips")
                    .setLore(Arrays.asList("§r",
                            "§7Current State: " + (!settings.allowingGameTips(player) ? "§cDisabled" : "§aEnabled"),
                            "§r", Utils.S + "Click to turn game tips " + (settings.allowingGameTips(player) ? "§coff" : "§aon") + Utils.S + ".")).toItemStack());
            inventory.setItem(13, new ItemBuilder(Material.WATCH).setName("§6Show Scoreboard")
                    .setLore(Arrays.asList("§r",
                            "§7Current State: " + (!settings.showScoreboard(player) ? "§cDisabled" : "§aEnabled"),
                            "§r", Utils.S + "Click to turn side scoreboards " + (settings.showScoreboard(player) ? "§coff" : "§aon") + Utils.S + ".")).toItemStack());
            inventory.setItem(16, new ItemBuilder(Material.WATCH).setName("§6Show Announcements")
                    .setLore(Arrays.asList("§r",
                            "§7Current State: " + (!settings.showAnnouncements(player) ? "§cDisabled" : "§aEnabled"),
                            "§r", Utils.S + "Click to turn automatic chat announcements " + (settings.showAnnouncements(player) ? "§coff" : "§aon") + Utils.S + ".")).toItemStack());
            inventory.setItem(4, new ItemBuilder(Material.ARROW).setName("§cGo Back")
                    .setLore("§7Click to go back.").toItemStack());
        }

        private void updateLobbyInventory(Inventory inventory, Player player) {
            inventory.setItem(11, new ItemBuilder(Material.DIAMOND_BOOTS).setName("§6Double Jump")
                    .setLore(Arrays.asList("§r",
                            "§7Current State: " + (!settings.lobbyDoubleJump(player) ? "§cDisabled" : "§aEnabled"),
                            "§r", Utils.S + "Click to turn double jump " + (settings.lobbyDoubleJump(player) ? "§coff" : "§aon") + Utils.S + ".")).toItemStack());
            inventory.setItem(15, new ItemBuilder(Material.WATCH).setName("§6Player Visibility")
                    .setLore(Arrays.asList("§r",
                            "§7Current State: " + (!settings.lobbyPlayerVisibility(player) ? "§cDisabled" : "§aEnabled"),
                            "§r", Utils.S + "Click to turn player visibility " + (settings.lobbyPlayerVisibility(player) ? "§coff" : "§aon") + Utils.S + ".")).toItemStack());
            inventory.setItem(4, new ItemBuilder(Material.ARROW).setName("§cGo Back")
                    .setLore("§7Click to go back.").toItemStack());
        }

}
