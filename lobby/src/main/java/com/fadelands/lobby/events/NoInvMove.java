package com.fadelands.lobby.events;

import com.fadelands.lobby.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class NoInvMove implements Listener {

    public Main plugin;
    public NoInvMove(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void disableInvMove(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!(plugin.buildMode.contains(player))) {
            event.setCancelled(true);
        }
    }
}
