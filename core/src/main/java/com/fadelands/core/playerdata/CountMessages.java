package com.fadelands.core.playerdata;

import com.fadelands.core.CorePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CountMessages implements Listener {

    public static String message;

    public CorePlugin plugin;
    public CountMessages(CorePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        PlayerData.Statistics stats = PlayerData.get(player.getUniqueId()).getStats();
        stats.setMessagesSent(stats.getMessagesSent() + 1);

    }
}
