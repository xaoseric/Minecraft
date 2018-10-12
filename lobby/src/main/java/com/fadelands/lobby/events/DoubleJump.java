package com.fadelands.lobby.events;

import com.fadelands.core.settings.Settings;
import com.fadelands.lobby.Main;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

public class DoubleJump implements Listener {

    private Main main;
    private Settings settings;

    public DoubleJump(Main main) {
        this.main = main;
        this.settings = new Settings();
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (settings.lobbyDoubleJump(player)) {
            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                return;
            }
            event.setCancelled(true);
            player.setAllowFlight(false);
            player.setFlying(false);
            Vector v = player.getLocation().getDirection().multiply(1.2f).setY(1.2);
            player.setVelocity(v);
            player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1, 0.2f);
            player.playEffect(player.getLocation(), Effect.FIREWORKS_SPARK, 10);
            player.playEffect(player.getLocation(), Effect.FLAME, 5);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (settings.lobbyDoubleJump(player)) {
            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                return;
            }
            if (!player.getAllowFlight()) {
                if(player.isOnGround()){
                    player.setAllowFlight(true);
                    }
                }
            }else{
            player.setAllowFlight(false);
            }
        }
    }


