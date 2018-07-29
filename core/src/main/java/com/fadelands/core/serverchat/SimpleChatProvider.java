package com.fadelands.core.serverchat;

import com.fadelands.core.CorePlugin;
import com.fadelands.core.serverchat.provider.ChatProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;

public class SimpleChatProvider implements ChatProvider {
    @Override
    public ComponentBuilder[] getFormat(Player player) {

        Chat chat = CorePlugin.getChat();
        Permission permission = CorePlugin.getPermissions();

        ComponentBuilder rankComponent = new ComponentBuilder(chat.getPlayerPrefix(player).replaceAll("&", "\u00a7"));
        rankComponent.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("\u00a73Click to view profile." +
                " \n" +
                "\u00a7r" +
                "\u00a77Rank\u00a7f: " + permission.getPrimaryGroup(player).replaceAll("&", "\u00a7") + "\n" +
                "\u00a77Level\u00a7f: " + "\u00a730" +
                "").create()));
        rankComponent.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/profile " + player.getName()));

        ComponentBuilder userComponent = new ComponentBuilder(player.getName());
        userComponent.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("\u00a73Click to message!").create()));
        userComponent.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + player.getName()));

        return new ComponentBuilder[]{rankComponent, userComponent, new ComponentBuilder("\u00a77: \u00a7r")};
    }
}
