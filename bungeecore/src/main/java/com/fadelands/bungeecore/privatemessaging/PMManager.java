package com.fadelands.bungeecore.privatemessaging;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;

public class PMManager {
    private static Map<String, String> lastMessage = new HashMap<>();
    public static ProxiedPlayer getLast(ProxiedPlayer player) {return ProxyServer.getInstance().getPlayer(lastMessage.get(player.getName()));}
    public static void setLast(ProxiedPlayer player, ProxiedPlayer value) {lastMessage.put(player.getName(), value.getName());}

}
