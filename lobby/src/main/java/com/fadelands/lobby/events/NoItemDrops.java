package com.fadelands.lobby.events;

import com.fadelands.lobby.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class NoItemDrops implements Listener {

    public Main plugin;
    public NoItemDrops(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void disableItemDropsOnDeath(PlayerDeathEvent event){
        event.getDrops().clear();
    }
}
