package com.fadelands.core.npcs;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.UUID;

public class NPCs implements Listener {

    public static void CreateNpc(Player playerWhoSpawnsNPC, String npcname) {

        String texture = "";
        String signature = "";

        Location loc = playerWhoSpawnsNPC.getLocation();
        double x = loc.getX();
        double y = loc.getY() + 1;
        double z = loc.getZ();
        float pitch = loc.getPitch();
        Vector dir = loc.getDirection();
        float yaw = loc.getYaw();

        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(UUID.randomUUID(), npcname), new PlayerInteractManager(nmsWorld));
        npc.setLocation(x, y, z, yaw, pitch);
        npc.getProfile().getProperties().put("textures", new Property("textures", SkinData.arrayofc.getTextures(), SkinData.arrayofc.getSignature()));

        PlayerConnection connection = ((CraftPlayer) playerWhoSpawnsNPC).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
    }

}