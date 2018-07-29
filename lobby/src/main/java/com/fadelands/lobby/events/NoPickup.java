package com.fadelands.lobby.events;

import com.fadelands.lobby.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class NoPickup implements Listener {

    public Main plugin;
    public NoPickup(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void disablePickup(PlayerPickupItemEvent event){
            if(!(plugin.buildMode.contains(event.getPlayer()))){
                event.setCancelled(true);
            }

        }
    }
