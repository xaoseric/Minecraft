package com.fadelands.array.playerdata;

import com.fadelands.array.Array;
import com.fadelands.array.achievements.Achievement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class CountBlocksPlaced implements Listener {

    public Array plugin;
    public CountBlocksPlaced(Array plugin)
    {
        this.plugin = plugin;
    }
    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PlayerData.Statistics stats = PlayerData.get(player.getUniqueId()).getStats();
        stats.setBlocksPlaced(stats.getBlocksPlaced() + 1);

        PlayerData playerdata = PlayerData.get(player.getUniqueId());

        if (playerdata == null) {
            return;
        }

        playerdata.getAhievementData().progress(Achievement.BLOCK_PLACER_I);
        playerdata.getAhievementData().progress(Achievement.BLOCK_PLACER_II);
        playerdata.getAhievementData().progress(Achievement.BLOCK_PLACER_III);
        playerdata.getAhievementData().progress(Achievement.BLOCK_PLACER_IV);
        playerdata.getAhievementData().progress(Achievement.BLOCK_PLACER_V);

    }
}