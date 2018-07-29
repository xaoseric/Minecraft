package com.fadelands.core.playerdata;

import com.fadelands.core.CorePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class CountBlocksRemoved implements Listener {

    public CorePlugin plugin;

    public CountBlocksRemoved(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerData.Statistics stats = PlayerData.get(player.getUniqueId()).getStats();
        stats.setBlocksRemoved(stats.getBlocksRemoved() + 1);
    }
}