package com.fadelands.core.provider.chat.announcements;

import com.fadelands.core.CorePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AnnouncementListener implements Listener {

    private CorePlugin plugin;
    public AnnouncementListener(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getAnnouncements().startAnnouncements(event.getPlayer());
    }
}
