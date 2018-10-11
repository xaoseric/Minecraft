package com.fadelands.array.provider.chat.announcements;

import com.fadelands.array.Array;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AnnouncementListener implements Listener {

    private Array plugin;
    public AnnouncementListener(Array plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getAnnouncements().startAnnouncements(event.getPlayer());
    }
}
