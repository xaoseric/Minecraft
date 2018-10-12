package com.fadelands.core.events;

import com.fadelands.core.Core;
import com.fadelands.core.player.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {

    private Core corePlugin;

    public Events(Core plugin) {
        this.corePlugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(new User().isDonatorRank(event.getPlayer().getName())) {
            event.setJoinMessage("§8[§2+§8] §a" + event.getPlayer().getName());
        }else{
            event.setJoinMessage("");
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (new User().isDonatorRank(event.getPlayer().getName())) {
            event.setQuitMessage("§8[§4-§8] §c" + event.getPlayer().getName());
        } else {
            event.setQuitMessage("");
        }
    }
}
