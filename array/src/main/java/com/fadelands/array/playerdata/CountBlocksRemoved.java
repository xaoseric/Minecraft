package com.fadelands.array.playerdata;

import com.fadelands.array.Array;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class CountBlocksRemoved implements Listener {

    public Array plugin;

    public CountBlocksRemoved(Array plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerData.Statistics stats = PlayerData.get(player.getUniqueId()).getStats();
        stats.setBlocksRemoved(stats.getBlocksRemoved() + 1);
    }
}