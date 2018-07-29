package com.fadelands.lobby.events;

import com.fadelands.lobby.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class NoBlockPlace implements Listener {

    public Main plugin;
    public NoBlockPlace(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void disableBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if(!(plugin.buildMode.contains(player))){
            event.setCancelled(true);
        }
    }
}
