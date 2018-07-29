package com.fadelands.lobby.events;

import com.fadelands.lobby.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class NoBlockBreak implements Listener {

    public Main plugin;
    public NoBlockBreak(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void disableBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(!(plugin.buildMode.contains(player))){
            event.setCancelled(true);
        }
    }
}
