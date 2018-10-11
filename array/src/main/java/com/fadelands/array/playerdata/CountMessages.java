package com.fadelands.array.playerdata;

import com.fadelands.array.Array;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CountMessages implements Listener {

    public static String message;

    public Array plugin;
    public CountMessages(Array plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        PlayerData.Statistics stats = PlayerData.get(player.getUniqueId()).getStats();
        stats.setMessagesSent(stats.getMessagesSent() + 1);

    }
}
