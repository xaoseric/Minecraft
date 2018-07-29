package com.fadelands.core.playerdata;

import com.fadelands.core.CorePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CountLogins implements Listener {

    public CorePlugin plugin;

    public CountLogins(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData.Statistics stats = PlayerData.get(player.getUniqueId()).getStats();
        stats.setLoginCount(stats.getLoginCount() + 1);

        }
    }
