package com.fadelands.core.provider.tablist;

import com.fadelands.core.CorePlugin;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Field;

public class TablistText implements Listener {

    public CorePlugin plugin;
    public TablistText(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

    PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

    try{
        Field headerfield = packet.getClass().getDeclaredField("a");
        headerfield.setAccessible(true);
        headerfield.set(packet, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("header-text")) + "\"}"));

        Field footerfield = packet.getClass().getDeclaredField("b");
        footerfield.setAccessible(true);
        footerfield.set(packet, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("footer-text")) + "\"}"));

    }catch (Exception e) {
        e.printStackTrace();
    }
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
}
