package com.fadelands.lobby.events;

import com.fadelands.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DisableVoid implements Listener {

    public Main plugin;
    public DisableVoid(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void disableVoid(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            if(event.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
                World world = Bukkit.getServer().getWorld("world");
                event.getEntity().teleport(world.getSpawnLocation());
                event.getEntity().sendMessage("§eWoah there, watch out for the void!");
            }
        }
    }
}
