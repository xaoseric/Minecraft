package com.fadelands.lobby.events;

import com.fadelands.lobby.Main;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

public class LobbyEvents implements Listener {

    private Main main;
    public static Location spawnLoc;


    public LobbyEvents(Main main){
        this.main = main;

        double yawd = main.getConfig().getDouble("yaw");
        double pitchd = main.getConfig().getDouble("pitch");
        float yaw = (float)yawd;
        float pitch = (float)pitchd;

        spawnLoc = new Location(Bukkit.getWorld("world")
                , main.getConfig().getDouble("x")
                , main.getConfig().getDouble("y")
                , main.getConfig().getDouble("z")
                , yaw, pitch);
    }

    @EventHandler
    public void noBlockBreak(BlockBreakEvent event){
        if(!(main.buildMode.contains(event.getPlayer()))){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void noBlockPlace(BlockPlaceEvent event) {
        if (!(main.buildMode.contains(event.getPlayer()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void noDamage(EntityDamageEvent event) {
        if(event.getEntityType() == EntityType.PLAYER){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void noInvMove(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!(main.buildMode.contains(player))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void disableItemDropsOnDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
    }

    @EventHandler
    public void disablePickup(PlayerPickupItemEvent event) {
        if (!(main.buildMode.contains(event.getPlayer()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void noWeatherChange(WeatherChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void disableSaturation(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void disableVoid(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                event.getEntity().teleport(spawnLoc);
                event.getEntity().sendMessage("§eWoah there, watch out for the void!");
            }
        }
    }

    @EventHandler
    public void disableBlockDamage(BlockDamageEvent event) {
        if (!(main.buildMode.contains(event.getPlayer()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void disableCropBreaking(PlayerInteractEvent event) {
        if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL)
            event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(spawnLoc);

        event.setJoinMessage(null);
        JoinItems joinItems = new JoinItems(main);
        joinItems.getJoinItems(player);

        player.sendMessage("§r" + "\n" +
                "§8§l┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" + "\n" +
                "            §fWelcome to §lFade§6§lLands " + "\n" +
                "§r " + "\n" +
                " §7      Use your compass to travel to different" + "\n" +
                "               §7" + "gamemodes and servers." + "\n" +
                "§r " + "\n" +
                "            §7You can find our rules, server" + "\n" +
                "         §7info and store at §2www.fadelands.com§7." + "\n" +
                "§8§l┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + "\n" +
                "§r ");
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        Location location = event.getTo();
        if(location.getBlockX() < 240 || location.getBlockX() > 640 || location.getBlockZ() < -690 || location.getBlockZ() > -290) {
            Player player = event.getPlayer();
            player.sendMessage("§eYou have reached the end of the lobby!");
            player.teleport(event.getFrom().getBlock().getLocation());
            player.playEffect(player.getLocation(), Effect.LARGE_SMOKE, 2);
            player.playEffect(player.getLocation(), Effect.EXTINGUISH, 3);
            player.playEffect(player.getLocation(), Effect.FLAME, 2);
        }
    }
}
