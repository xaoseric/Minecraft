package com.fadelands.array.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import javax.sql.rowset.CachedRowSet;
import java.io.EOFException;
import java.util.Map;
import java.util.UUID;

public class PluginMessage implements PluginMessageListener {

    private Map<UUID, String> playerServerMap;
    private Map<String, Integer> playerCountMap;
    private Map<String, String[]> playerMap;

    public JavaPlugin plugin;

    public PluginMessage(JavaPlugin plugin) {
        this.plugin = plugin;
        playerServerMap = Maps.newHashMap();
        playerCountMap = Maps.newHashMap();
        playerMap = Maps.newHashMap();
        ping("ALL");
        ping("LOBBY");
        ping("SB-AIR");

    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();

        try {
            if (subChannel.equals("PlayerCount")) {
                String server = in.readUTF();
                playerCountMap.put(server, in.readInt());
            }
        } catch (Exception ignored) {}

        try {
            if (subChannel.equals("GetServer")) {
                String server = in.readUTF();
                playerServerMap.put(player.getUniqueId(), server);
            }
        } catch (Exception ignored) {}

        try {
            if (subChannel.equals("PlayList")) {
                String server = in.readUTF();
                String[] playerList = in.readUTF().split(", ");
                playerMap.put(server, playerList);
            }
        } catch (Exception ignored) {}
    }

    public Integer getPlayers(String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("PlayerCount");
        out.writeUTF(server);
        Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

        return playerCountMap.get(server) == null ? 0 : playerCountMap.get(server);
    }

    public String getServerName(Player player) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("GetServer");
        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());

        return playerServerMap.get(player.getUniqueId()) == null ? "Unknown" : playerServerMap.get(player.getUniqueId());
    }

    public String[] getPlayerNames(String server) {
        ping(server);
        try {
            return ImmutableMap.copyOf(playerMap).get(server);
        } catch (Exception ignored) {}

        return null;
    }

    private void ping(String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF(server);

        Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    public void sendToServer(Player messageSender, String server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("Connect");
        output.writeUTF(server);

        messageSender.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

    public void kickPlayer(Player messageSender, String playerName, String kickReason) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("KickPlayer");
        output.writeUTF(playerName);
        output.writeUTF(kickReason);

        messageSender.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());

    }
}
