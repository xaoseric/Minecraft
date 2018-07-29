package com.fadelands.lobby.events;

import com.fadelands.lobby.Main;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoDamage implements Listener {

    public Main plugin;
    public NoDamage(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void disableDamage(EntityDamageEvent event){
        if(event.getEntityType() == EntityType.PLAYER){
            event.setCancelled(true);
        }
    }
}
