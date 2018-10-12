package com.fadelands.core.provider.chat.announcements;

import com.fadelands.core.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AnnouncementListener implements Listener {

    private Core plugin;
    public AnnouncementListener(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getAnnouncements().startAnnouncements(event.getPlayer());
    }
}
