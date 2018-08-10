package com.fadelands.lobby.guis;

import com.fadelands.array.Array;
import com.fadelands.array.plmessaging.PluginMessage;
import com.fadelands.lobby.Main;
import com.fadelands.lobby.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ServerSelectorGui implements Listener {

    private PluginMessage pluginMessage = Array.plugin.getPluginMessage();

    public Main plugin;

    public ServerSelectorGui(Main plugin) {
        this.plugin = plugin;
    }

    private final static String invName = "§lSelect a Gamemode";

    private static ItemStack SkyBlockRealms = new ItemBuilder(Material.MAP).setName("§3§lSkyblock Realms").setLore(Arrays.asList("§r",
            "§7Click to view our SkyBlock Realms!")).toItemStack();

    private static ItemStack SurvivalRealms = new ItemBuilder(Material.MAP).setName("§3§lSurvival Realms").setLore(Arrays.asList("§r", "§c§l(!) §cUndergoing Development!",
            "§7Click to view our Survival Realms!")).toItemStack();

    public static void openServerSelector(Player player) {
        Inventory selector = Bukkit.createInventory(null, 9 * 3, invName);
        selector.clear();

        selector.setItem(12, SkyBlockRealms);
        selector.setItem(14, SurvivalRealms);

        player.openInventory(selector);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(invName)) {
            event.setCancelled(true);

            if (event.getCurrentItem().isSimilar(SkyBlockRealms)) {
                SkyblockGui.openRealmSelector(player);
            }
            if (event.getCurrentItem().isSimilar(SurvivalRealms)) {
                player.sendMessage("§cOur Survival Realms are currently under heavy development! Keep an eye out for announcements on our forums or Discord!");
            }
        }
    }
}