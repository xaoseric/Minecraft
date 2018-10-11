package com.fadelands.array.playerdata;

import com.fadelands.array.Array;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CountLogins implements Listener {

    public Array plugin;

    public CountLogins(Array plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData.Statistics stats = PlayerData.get(player.getUniqueId()).getStats();
        stats.setLoginCount(stats.getLoginCount() + 1);

        }
    }
