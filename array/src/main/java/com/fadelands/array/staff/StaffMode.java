package com.fadelands.array.staff;

import com.fadelands.array.player.User;
import com.fadelands.array.utils.ItemBuilder;
import com.fadelands.array.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.UUID;

public class StaffMode implements Listener {

    private HashMap<UUID, ItemStack[]> inventoryItemsHashMap = new HashMap<>();
    private HashMap<UUID, ItemStack[]> inventoryArmorHashMap = new HashMap<>();
    private HashMap<UUID, Location> playerLocation = new HashMap<>();

    private StaffSettings settings;

    public StaffMode(StaffSettings settings) {
        this.settings = settings;
    }

    public void toggleOn(Player player) {
        if (new User().isStaff(player.getName())) {
            PlayerInventory playerInventory = player.getInventory();
            Location location = player.getLocation();

            if (inventoryItemsHashMap.containsKey(player.getUniqueId()) || inventoryArmorHashMap.containsKey(player.getUniqueId()) || playerLocation.containsKey(player.getUniqueId())) return;
                inventoryItemsHashMap.put(player.getUniqueId(), playerInventory.getContents());
                inventoryArmorHashMap.put(player.getUniqueId(), playerInventory.getArmorContents());
                playerLocation.put(player.getUniqueId(), location);

                playerInventory.clear();

                playerInventory.setHelmet(new ItemStack(Material.AIR));
                playerInventory.setChestplate(new ItemStack(Material.AIR));
                playerInventory.setLeggings(new ItemStack(Material.AIR));
                playerInventory.setBoots(new ItemStack(Material.AIR));

                playerInventory.setItem(0, new ItemBuilder(Material.REDSTONE).setName("§c§lVanish On §7(Click to toggle)").setLore("§7Press to toggle off.").toItemStack());
                playerInventory.setItem(6, new ItemBuilder(Material.COMPASS).setName("§6Compass").setLore("§7Right click to go through", "§7blocks, left click to tp to your aim.").toItemStack());
                playerInventory.setItem(7, new ItemBuilder(Material.BOOK).setName("§6Show Inventory").setLore("§7Right click a player to", "§7view their inventory.").toItemStack());
                playerInventory.setItem(8, new ItemBuilder(Material.PAPER).setName("§6Staff Settings").setLore("§7Right click to change", "§7your settings.").toItemStack());

                player.setGameMode(GameMode.CREATIVE);
                player.setFlying(true);
                player.hidePlayer(player);

                player.sendMessage(Utils.Prefix + "§2You entered staff mode.");
        }
    }

    public void toggleOff(Player player) {
        if(inventoryItemsHashMap.containsKey(player.getUniqueId()) ||
                inventoryArmorHashMap.containsKey(player.getUniqueId()) || playerLocation.containsKey(player.getUniqueId())) {

            Location oldLocation = playerLocation.get(player.getUniqueId());
            ItemStack[] oldInventoryItems = inventoryItemsHashMap.get(player.getUniqueId());
            ItemStack[] oldArmor = inventoryArmorHashMap.get(player.getUniqueId());

            PlayerInventory playerInventory = player.getInventory();

            playerInventory.clear();

            playerInventory.setContents(oldInventoryItems);
            playerInventory.setArmorContents(oldArmor);
            player.updateInventory();

            player.setGameMode(GameMode.SURVIVAL);
            player.setFlying(false);
            player.showPlayer(player);

            player.teleport(oldLocation);

            playerLocation.remove(player.getUniqueId());
            inventoryItemsHashMap.remove(player.getUniqueId());
            inventoryArmorHashMap.remove(player.getUniqueId());

            player.sendMessage(Utils.Prefix + "§cYou left staff mode.");
        }
    }

    @EventHandler
    public void noBlockBreak(BlockBreakEvent event){
        if(settings.vanishOn(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void noBlockPlace(BlockPlaceEvent event) {
        if(settings.vanishOn(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void noDamage(PlayerInteractEvent event) {
        if(settings.vanishOn(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void noInvMove(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(settings.vanishOn(player)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void disableItemDropsOnDeath(PlayerDeathEvent event) {
        if (settings.vanishOn(event.getEntity())) {
            event.getDrops().clear();
        }
    }

    @EventHandler
    public void disablePickup(PlayerPickupItemEvent event) {
        if(settings.vanishOn(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void disableDrop(PlayerDropItemEvent event) {
        if(settings.vanishOn(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void disableBlockDamage(BlockDamageEvent event) {
        if(settings.vanishOn(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void disableCropBreaking(PlayerInteractEvent event) {
        if (settings.vanishOn(event.getPlayer())) {
            if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if(inventoryArmorHashMap.containsKey(event.getPlayer().getUniqueId()) || inventoryItemsHashMap.containsKey(event.getPlayer().getUniqueId()) || playerLocation.containsKey(event.getPlayer().getUniqueId())) {
            inventoryItemsHashMap.remove(event.getPlayer().getUniqueId());
            inventoryArmorHashMap.remove(event.getPlayer().getUniqueId());
            playerLocation.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(settings.vanishOn(event.getPlayer())) {
            toggleOn(event.getPlayer());
            event.getPlayer().sendMessage(Utils.Prefix + "§aLast time you left, you were in vanish. You have been put in vanish again. You can toggle off with /vanish.");
        }
    }
}
