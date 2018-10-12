package com.fadelands.core.playerdata;

import com.fadelands.core.Core;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class CountDeaths implements Listener {

    public Core plugin;

    public CountDeaths(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.getType() == EntityType.PLAYER) {

            PlayerData.Statistics stats = PlayerData.get(entity.getUniqueId()).getStats();
            stats.setDeaths(stats.getDeaths() + 1);
        }
    }
}

