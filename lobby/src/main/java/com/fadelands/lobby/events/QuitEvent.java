package com.fadelands.lobby.events;

import com.fadelands.lobby.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    public Main plugin;
    public QuitEvent(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onquit(PlayerQuitEvent event){
        event.setQuitMessage(null);
    }

}
