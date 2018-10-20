package com.fadelands.core.commands;

import com.fadelands.core.Core;
import com.fadelands.core.achievements.Achievement;
import com.fadelands.core.playerdata.PlayerData;
import com.fadelands.core.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Objects;

public class CommandProcess implements Listener {

    public Core plugin;

    public CommandProcess(Core plugin) {
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
        if (!event.getPlayer().isOp() && (msg.toLowerCase().contains("/me")
                || msg.toLowerCase().startsWith("/minecraft:") || msg.toLowerCase().startsWith("/minecraft")
                || msg.toLowerCase().startsWith("/bukkit:") || msg.toLowerCase().startsWith("/bukkit")
                || msg.toLowerCase().startsWith("/version") || msg.toLowerCase().startsWith("/ver")
                || msg.toLowerCase().startsWith("/about") || msg.toLowerCase().startsWith("/bukkit:about")
                || msg.toLowerCase().equals("/pl") || msg.toLowerCase().startsWith("/plugins")
                || msg.toLowerCase().startsWith("/bukkit:ver") || msg.toLowerCase().startsWith("/?"))) {
            event.getPlayer().sendMessage(Utils.Prefix + "§cUnknown command. Type \"/help\" for help.");
            event.setCancelled(true);
        }

       PlayerData playerData = PlayerData.get(event.getPlayer().getUniqueId());
        if (playerData != null) {
            playerData.getStats().setCommandsUsed(playerData.getStats().getCommandsUsed() + 1);
        }
        plugin.getAchManager().startAchievement(event.getPlayer(), Achievement.FIRST_COMMAND);
    }

}
