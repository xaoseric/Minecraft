package com.fadelands.array.playerdata;

import com.fadelands.array.Array;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class CountDeaths implements Listener {

    public Array plugin;

    public CountDeaths(Array plugin) {
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

