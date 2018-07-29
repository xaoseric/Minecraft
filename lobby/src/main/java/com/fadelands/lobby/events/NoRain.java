package com.fadelands.lobby.events;

import com.fadelands.lobby.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class NoRain implements Listener {

    public Main plugin;
    public NoRain(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void disableRain(WeatherChangeEvent event){
        event.setCancelled(true);
    }
}
