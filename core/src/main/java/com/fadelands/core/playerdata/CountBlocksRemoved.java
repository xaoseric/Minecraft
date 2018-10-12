package com.fadelands.core.playerdata;

import com.fadelands.core.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class CountBlocksRemoved implements Listener {

    public Core plugin;

    public CountBlocksRemoved(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerData.Statistics stats = PlayerData.get(player.getUniqueId()).getStats();
        stats.setBlocksRemoved(stats.getBlocksRemoved() + 1);
    }
}