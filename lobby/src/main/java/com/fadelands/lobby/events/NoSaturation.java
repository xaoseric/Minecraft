package com.fadelands.lobby.events;

import com.fadelands.lobby.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class NoSaturation implements Listener {

    public Main plugin;
    public NoSaturation(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void disableSaturation(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }
}
