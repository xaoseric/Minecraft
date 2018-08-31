package com.fadelands.core.provider.chat;

import com.fadelands.array.utils.Utils;
import com.fadelands.core.CorePlugin;
import com.fadelands.core.provider.chat.provider.ChatProvider;
import net.md_5.bungee.api.chat.*;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;

public class SimpleChatProvider implements ChatProvider {
    @Override
    public ComponentBuilder[] getFormat(Player player) {

        Chat chat = CorePlugin.getChat();
        Permission permission = CorePlugin.getPermissions();

        ComponentBuilder levelComponent = new ComponentBuilder("§71 ");
        levelComponent.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.NETWORK_LEVEL_MSG
        .replace("{0}", "1")).create()));

        ComponentBuilder rankComponent = new ComponentBuilder(chat.getPlayerPrefix(player).replaceAll("&", "\u00a7"));
        rankComponent.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.USER_BLOCK
                .replace("{0}", permission.getPrimaryGroup(player).toUpperCase()
                .replaceAll("&", "§"))
                .replace("{1}", "1"))
                .create()));
        rankComponent.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/profile " + player.getName()));

        ComponentBuilder userComponent = new ComponentBuilder(player.getName());
        userComponent.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.USER_BLOCK
                .replace("{0}", permission.getPrimaryGroup(player).toUpperCase()
                        .replaceAll("&", "§"))
                .replace("{1}", "1"))
                .create()));
        userComponent.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/profile " + player.getName()));

        return new ComponentBuilder[]{levelComponent, rankComponent, userComponent, new ComponentBuilder("\u00a77: \u00a7r")};
    }
}
