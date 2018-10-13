package com.fadelands.core.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PluginMessage implements PluginMessageListener {

    private Map<UUID, String> playerServerMap;
    private Map<String, Integer> playerCountMap;
    private Map<String, String[]> playerMap;
    private Map<String, String[]> playerListMap;

    public JavaPlugin plugin;

    public PluginMessage(JavaPlugin plugin) {
        this.plugin = plugin;
        playerServerMap = Maps.newHashMap();
        playerCountMap = Maps.newHashMap();
        playerMap = Maps.newHashMap();
        playerListMap = Maps.newHashMap();
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

        try {
            if (subChannel.equals("PlayerList")) {
                String server = in.readUTF();
                String[] players = in.readUTF().split(", ");
                playerListMap.put(server, players);
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

    public void sendToServer(Player target, String server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("Connect");
        output.writeUTF(server);

        target.sendMessage(Utils.Prefix + "§2Sending you to §a" + server + "§2.");
        target.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

    public void sendToServer(Player sender, Player target, String server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("Connect");
        output.writeUTF(server);

        target.sendMessage(Utils.Prefix + "§2You were sent to §a" + server + " §2by " + sender.getName() + ".");
        target.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

    public void kickPlayer(String playerName, String kickReason) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("KickPlayer");
        output.writeUTF(playerName);
        output.writeUTF(kickReason);

        Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

    public void sendMessage(String playerName, String text) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("Message");
        output.writeUTF(playerName);
        output.writeUTF(text);

        Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

    public void sendMessageToAll(Player sender, String text) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("Message");
        output.writeUTF("ALL");
        output.writeUTF(text);

        sender.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

    public boolean isOnline(String username) {
        return Arrays.stream(getPlayerList()).collect(Collectors.toList()).contains(username);

    }

    public String[] getPlayerList() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF("ALL");

        Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        return playerListMap.get("ALL");

    }
}
