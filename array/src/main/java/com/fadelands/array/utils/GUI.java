package com.fadelands.array.utils;

import com.fadelands.array.Array;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class GUI implements Listener {

    private Inventory inventory;
    private String name;
    private Plugin plugin;

    public GUI(Plugin plugin, String name, int size, int update) {
        this.inventory = Bukkit.createInventory(null, size, name);
        this.name = name;
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        updateInventory(this.inventory, this.plugin);
        if (update != 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    updateInventory(inventory, plugin);
                }
            }.runTaskTimerAsynchronously(plugin, update, update);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getInventory().getTitle().equals(this.name)) {
            event.setCancelled(true);
            handleInventoryClickEvent(event, this.plugin);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        handleInventoryCloseEvent(event, this.plugin);
    }

    public abstract void handleInventoryClickEvent(InventoryClickEvent event, Plugin plugin);

    public abstract void handleInventoryCloseEvent(InventoryCloseEvent event, Plugin plugin);

    public abstract void updateInventory(Inventory inventory, Plugin plugin);
}