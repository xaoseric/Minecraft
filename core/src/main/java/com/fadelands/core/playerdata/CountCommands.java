package com.fadelands.core.playerdata;

import com.fadelands.core.CorePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CountCommands implements Listener {

    public CorePlugin plugin;
    public CountCommands(CorePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();
        PlayerData.Statistics stats = PlayerData.get(player.getUniqueId()).getStats();
        stats.setCommandsUsed(stats.getCommandsUsed() + 1);

        }
    }