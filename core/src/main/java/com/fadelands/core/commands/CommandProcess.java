package com.fadelands.core.commands;

import com.fadelands.core.CorePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandProcess implements Listener {

    public CorePlugin plugin;
    public CommandProcess(CorePlugin plugin) {

    this.plugin = plugin;

}

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerCommandWhileDeadEvent(PlayerCommandPreprocessEvent event) {
        // Fix Bukkit race condition
        if (event.getPlayer().isDead()) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage();
        if(!event.getPlayer().isOp() && ( msg.toLowerCase().startsWith("/me")
                    || msg.toLowerCase().startsWith("/minecraft:") || msg.toLowerCase().startsWith("/minecraft")
                    || msg.toLowerCase().startsWith("/bukkit:") || msg.toLowerCase().startsWith("/bukkit")
                    || msg.toLowerCase().startsWith("/version") || msg.toLowerCase().startsWith("/ver")
                    || msg.toLowerCase().startsWith("/bukkit:ver") || msg.toLowerCase().startsWith("/?"))){
            event.getPlayer().sendMessage("Unknown command. Type \"/help\" for help.");
            event.setCancelled(true);
        }else if (msg.equalsIgnoreCase("/list")) {
            event.setCancelled(true);
        }
    }
}
