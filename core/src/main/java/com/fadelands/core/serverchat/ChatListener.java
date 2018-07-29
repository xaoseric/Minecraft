package com.fadelands.core.serverchat;

import com.fadelands.core.CorePlugin;
import com.fadelands.core.serverchat.provider.ChatProvider;
import com.google.common.collect.ImmutableSet;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ChatProvider chatProvider = CorePlugin.getInstance().getChatProvider();
        event.setCancelled(true);

        ComponentBuilder[] format = chatProvider.getFormat(player);
        List<BaseComponent> components = new ArrayList<>();

        for (int i = 0; i < format.length; i++) {
            components.addAll(Arrays.asList(format[i].create()));
        }

        components.add(new TextComponent(event.getMessage()));

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.spigot().sendMessage(components.toArray(new BaseComponent[components.size()]));
        }
    }
}