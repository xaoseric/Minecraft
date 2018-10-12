package com.fadelands.core.playerdata;

import com.fadelands.core.Core;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CountKills implements Listener {

    public Core plugin;

    public CountKills(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDamageByEntityEvent event) {
        Entity murderEnt = event.getDamager();
        Entity entity = event.getEntity();

        if(murderEnt.getType() == EntityType.PLAYER){
            if(entity.getType() == EntityType.PLAYER && (event.getEntity().isDead())){

            PlayerData.Statistics stats = PlayerData.get(murderEnt.getUniqueId()).getStats();
            stats.setKills(stats.getKills() + 1);
        }
    }

    }
}