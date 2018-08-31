package com.fadelands.lobby.events;

import com.fadelands.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class LobbyEvents implements Listener {

    private Main main;

    public LobbyEvents(Main main){
        this.main = main;
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
                World world = Bukkit.getServer().getWorld("world");
                event.getEntity().teleport(world.getSpawnLocation());
                event.getEntity().sendMessage("§eWoah there, watch out for the void!");
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage(null);
        JoinItems joinItems = new JoinItems(main);
        joinItems.getJoinItems(player);

        player.sendMessage("§r" + "\n" +
                "§8§l┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" + "\n" +
                "            §fWelcome to §lFade§b§lLands " + "\n" +
                "§r " + "\n" +
                " §7      Use your compass to travel to different" + "\n" +
                "               §7" + "gamemodes and servers." + "\n" +
                "§r " + "\n" +
                "            §7You can find our rules, server" + "\n" +
                "         §7info and store at §bwww.fadelands.com§7." + "\n" +
                "§8§l┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + "\n" +
                "§r ");
    }
}
