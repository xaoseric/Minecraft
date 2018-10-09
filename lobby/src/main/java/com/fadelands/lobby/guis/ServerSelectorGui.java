package com.fadelands.lobby.guis;

import com.fadelands.array.Array;
import com.fadelands.array.utils.PluginMessage;
import com.fadelands.array.utils.Utils;
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

    private static PluginMessage pluginMessage = Array.plugin.getPluginMessage();

    public Main plugin;

    public ServerSelectorGui(Main plugin) {
        this.plugin = plugin;
    }

    private final static String invName = "Select a GameMode";

    private static ItemStack SkyBlockRealms = new ItemBuilder(Material.MAP).setName("§6§lSkyblock Realms").setLore(Arrays.asList("§r",
            "§7Click to view our SkyBlock Realms!")).toItemStack();

    private static ItemStack SurvivalRealms = new ItemBuilder(Material.MAP).setName("§6§lSurvival Realms").setLore(Arrays.asList("§r", "§c§l(!) §cUndergoing Development!",
            "§7Click to view our Survival Realms!")).toItemStack();

    private static ItemStack SMP = new ItemBuilder(Material.MAP).setName("§6§lSMP").setLore(Arrays.asList(
            "§2Players (" + pluginMessage.getPlayers("SMP") + "/100)",
            "§r",
            "§7Explore a massive unknown ",
            "§7world all by yourself or with",
            "§7your friends by your side...",
            "§7Build a village or what ever comes in",
            "§7your mind! It's up to your imagination!",
            "§r",
            "§2§lFeatures",
            "§r",
            "§a* §fBoosters",
            "§a* §fCustom Enchants",
            "§a* §fDaily Spins",
            "§r",
            "§6§l» §fClick to connect...")).toItemStack();


    public static void openServerSelector(Player player) {
        Inventory selector = Bukkit.createInventory(null, 9 * 5, invName);
        selector.clear();

        selector.setItem(11, SkyBlockRealms);
        selector.setItem(15, SurvivalRealms);
        selector.setItem(31, SMP);

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
                player.sendMessage(Utils.Prefix + "§cOur Survival Realms are currently under heavy development! Keep an eye out for announcements on our forums or Discord!");
            }
            if (event.getCurrentItem().isSimilar(SMP)) {
                player.sendMessage(Utils.Prefix + "§cOur SMP Server is currently under heavy development! Keep an eye out for announcements on our forums or Discord!");
            }
        }
    }
}