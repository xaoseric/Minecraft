package com.fadelands.array.plmessaging;

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

    public JavaPlugin plugin;

    public PluginMessage(JavaPlugin plugin) {
        this.plugin = plugin;
        playerServerMap = Maps.newHashMap();
        playerCountMap = Maps.newHashMap();
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

    public void sendToServer(Player player, String server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("Connect");
        output.writeUTF(server);

        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }
}
