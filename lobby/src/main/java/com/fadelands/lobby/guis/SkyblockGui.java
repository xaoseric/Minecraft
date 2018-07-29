package com.fadelands.lobby.guis;

import com.fadelands.array.Array;
import com.fadelands.array.plmessaging.PluginMessage;
import com.fadelands.lobby.Main;
import com.fadelands.lobby.Utils;
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

public class SkyblockGui implements Listener {

    private PluginMessage pluginMessage = Array.plugin.getPluginMessage();


    public Main plugin;

    public SkyblockGui(Main plugin) {
        this.plugin = plugin;
    }

    private final static String invName = "§lSelect a Realm";

    private static ItemStack airRealm = new ItemBuilder(Material.MILK_BUCKET).setName("§f§lAir Realm").setLore(Arrays.asList("§r",
            "§7Create your own Sky Empire, invite your friends",
            "§7to your sky island and try to reach the top!",
            "§7Can you make it to the top and become the",
            "§7biggest and richest sky empire?",
            "§r",
            "§b§lFeatures",
            "§r",
            "§3* §fBoosters",
            "§3* §fCustom Enchants",
            "§3* §fSupply Crates & Backpacks",
            "§3* §fDaily Spins",
            "§3* §fIsland Levels & More",
            "§r",
            "§b§l» §fClick to connect...")).toItemStack();

    private static ItemStack waterRealm = new ItemBuilder(Material.WATER_BUCKET).setName("§b§lWater Realm").setLore(Arrays.asList("§r",
            "§c§lCOMING SOON!",
            "§r",
            "§7Make your own Water Empire! But watch out,",
            "§7because the water is acid! Fall into it",
            "§7and you're dead! Ouch...",
            "§7It's a pretty big challenge,",
            "§7Can you handle it?",
            "§r",
            "§b§lFeatures",
            "§r",
            "§3* §fBoosters",
            "§3* §fCustom Enchants",
            "§3* §fSupply Crates & Backpacks",
            "§3* §fDaily Spins",
            "§3* §fAcid Water",
            "§3* §fIsland Levels & More",
            "§r",
            "§b§l» §fClick to connect...")).toItemStack();

    private static ItemStack back = new ItemBuilder(Material.BARRIER).setName("§cGo Back").setLore(Arrays.asList("§r",
            "§7Click to return to the server menu!")).toItemStack();

    public static void openRealmSelector(Player player) {
        Inventory selector = Bukkit.createInventory(null, 9 * 3, invName);
        selector.clear();

        selector.setItem(11, airRealm);
        selector.setItem(15, waterRealm);
        selector.setItem(22, back);
        player.openInventory(selector);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(invName)) {
            event.setCancelled(true);

            if (event.getCurrentItem().isSimilar(airRealm)) {
                pluginMessage.sendToServer(player, "SB-AIR");
                player.sendMessage(Utils.Prefix + "§3Sending you to the §bAir Realm§3..");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(waterRealm)) {
                player.sendMessage(Utils.Prefix + "§cWe are still working on this server, check out for announcements on our website!");
                player.closeInventory();
            }
            if (event.getCurrentItem().isSimilar(back)) {
                ServerSelectorGui.openServerSelector(player);
            }

        }
    }
}