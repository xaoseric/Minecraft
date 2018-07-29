package com.fadelands.core.playerdata;

import com.fadelands.core.CorePlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CountKills implements Listener {

    public CorePlugin plugin;

    public CountKills(CorePlugin plugin) {
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