package com.fadelands.sbair.actionbar;

import com.fadelands.sbair.Main;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar {

    public Main plugin;

    public ActionBar(Main plugin) {
        this.plugin = plugin;
    }

    private PacketPlayOutChat packet;


    public ActionBar(String text) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);
        this.packet = packet;

    }

    public void sentToPlayer(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

    }

    public void sentToAll() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

        }
    }
}
