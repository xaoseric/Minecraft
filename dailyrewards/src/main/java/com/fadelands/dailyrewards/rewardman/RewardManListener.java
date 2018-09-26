package com.fadelands.dailyrewards.rewardman;

import com.fadelands.dailyrewards.Main;
import com.fadelands.dailyrewards.rewardman.inventory.RewardInventory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class RewardManListener implements Listener {

    private Main plugin;

    public RewardManListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRewardManClick(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        if (entity.getType() == EntityType.VILLAGER) {
            if (entity.isCustomNameVisible() && (entity.getCustomName().equals("§6§lReward Man"))) {
                event.setCancelled(true);
                new RewardInventory(plugin).openInventory(player);
            }
        }
    }
}