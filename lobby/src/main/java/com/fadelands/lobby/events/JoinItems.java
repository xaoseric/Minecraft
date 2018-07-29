package com.fadelands.lobby.events;

import com.fadelands.lobby.Main;
import com.fadelands.lobby.guis.ServerSelectorGui;
import com.fadelands.lobby.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class JoinItems implements Listener {

    public static String selectorName = "§b§lPlay §7(Right-Click)";
    public static String profileName = "§6§lProfile §7(Right-Click)";

    public Main plugin;

    public JoinItems(Main main) {
        this.plugin = plugin;
    }

    public static void getJoinItems(Player player) {

        ItemStack selector = new ItemBuilder(Material.COMPASS).setName(selectorName).setAmount(1).toItemStack();
        ItemStack profile = new ItemBuilder(Material.SKULL_ITEM).setData(3).setSkullOwner(player.getName()).setName(profileName).setAmount(1).toItemStack();

        PlayerInventory inv = player.getInventory();
        inv.clear();
        inv.setHelmet(null);
        inv.setChestplate(null);
        inv.setLeggings(null);
        inv.setBoots(null);

        inv.setItem(3, selector);
        inv.setItem(5, profile);

    }

    @EventHandler
    @Deprecated
    public void onDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().hasItemMeta()) {
            if (event.getItemDrop().getItemStack().getItemMeta().getDisplayName() == selectorName ||
                    event.getItemDrop().getItemStack().getItemMeta().getDisplayName() == (profileName)){
                event.setCancelled(true);
            }
        }
    }
    @Deprecated
    @EventHandler(priority=EventPriority.HIGH)
    public void onRightClickItems(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(!player.getItemInHand().hasItemMeta()) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (player.getItemInHand().getItemMeta().getDisplayName().equals(selectorName)) {
                    ServerSelectorGui.openServerSelector(player);
                }
                if (player.getItemInHand().getItemMeta().getDisplayName().equals(profileName)) {
                    player.performCommand("profile");

                }
            }

        }
    }

