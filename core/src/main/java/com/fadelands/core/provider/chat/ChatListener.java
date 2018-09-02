package com.fadelands.core.provider.chat;

import com.fadelands.array.Array;
import com.fadelands.array.utils.Utils;
import com.fadelands.core.CorePlugin;
import com.fadelands.core.provider.chat.provider.ChatProvider;
import com.fadelands.core.settings.Settings;
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
        if (event.isCancelled()) return;
        event.setCancelled(true);
        Settings settings = new Settings();

        if(!settings.publicChat(player)){
            player.sendMessage(Utils.Prefix + "§cYou have toggled public chat off! To chat with others, change your settings with /settings.");
            return;
        }

        ComponentBuilder[] format = chatProvider.getFormat(player);
        List<BaseComponent> components = new ArrayList<>();

        for (int i = 0; i < format.length; i++) {
            components.addAll(Arrays.asList(format[i].create()));
        }

        components.add(new TextComponent(event.getMessage()));

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (settings.publicChat(online)) {
                online.spigot().sendMessage(components.toArray(new BaseComponent[components.size()]));
            }
        }
    }
}