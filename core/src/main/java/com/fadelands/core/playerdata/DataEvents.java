package com.fadelands.core.playerdata;

import com.fadelands.core.Core;
import com.fadelands.core.achievements.Achievement;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class DataEvents implements Listener {

    private Core plugin;

    public DataEvents(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerData.get(player.getUniqueId());

        if (playerData == null) {
            return;
        }

        playerData.getStats().setBlocksPlaced(playerData.getStats().getBlocksPlaced() + 1);

        playerData.getAchivementData().progress(Achievement.BLOCK_PLACER_I);
        playerData.getAchivementData().progress(Achievement.BLOCK_PLACER_II);
        playerData.getAchivementData().progress(Achievement.BLOCK_PLACER_III);
        playerData.getAchivementData().progress(Achievement.BLOCK_PLACER_IV);
        playerData.getAchivementData().progress(Achievement.BLOCK_PLACER_V);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerData.get(player.getUniqueId());

        if (playerData == null) {
            return;
        }

        playerData.getStats().setBlocksRemoved(playerData.getStats().getBlocksRemoved() + 1);

        playerData.getAchivementData().progress(Achievement.GRIEFER_I);
        playerData.getAchivementData().progress(Achievement.GRIEFER_II);
        playerData.getAchivementData().progress(Achievement.GRIEFER_III);
        playerData.getAchivementData().progress(Achievement.GRIEFER_IV);
        playerData.getAchivementData().progress(Achievement.GRIEFER_V);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) {

            PlayerData playerData = PlayerData.get(entity.getUniqueId());

            if (playerData == null) {
                return;
            }

            playerData.getStats().setDeaths(playerData.getStats().getDeaths() + 1);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDamageByEntityEvent event) {
        Entity killer = event.getDamager();
        Entity entity = event.getEntity();

        if (killer instanceof Player) {
            if (entity instanceof Player && (entity.isDead())) {

                PlayerData playerData = PlayerData.get(entity.getUniqueId());

                if (playerData == null) {
                    return;
                }

                playerData.getStats().setKills(playerData.getStats().getKills() + 1);

                playerData.getAchivementData().progress(Achievement.SERIAL_KILLER_I);
                playerData.getAchivementData().progress(Achievement.SERIAL_KILLER_II);
                playerData.getAchivementData().progress(Achievement.SERIAL_KILLER_III);
                playerData.getAchivementData().progress(Achievement.SERIAL_KILLER_IV);
                playerData.getAchivementData().progress(Achievement.SERIAL_KILLER_V);
            }
        }
    }
}
