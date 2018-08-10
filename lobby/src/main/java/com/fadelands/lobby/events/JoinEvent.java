package com.fadelands.lobby.events;

import com.fadelands.lobby.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    public Main plugin;
    public JoinEvent(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage(null);
        JoinItems joinItems = new JoinItems(plugin);
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
