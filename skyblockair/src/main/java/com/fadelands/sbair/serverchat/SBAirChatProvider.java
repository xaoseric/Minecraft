package com.fadelands.sbair.serverchat;

import com.fadelands.array.plmessaging.PluginMessage;
import com.fadelands.array.utils.Utils;
import com.fadelands.core.CorePlugin;
import com.fadelands.core.serverchat.provider.ChatProvider;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;

import static com.fadelands.sbair.Main.asbAPI;

@SuppressWarnings("Duplicates")
public class SBAirChatProvider implements ChatProvider {
    public SBAirChatProvider() {
    }

    @Override
    public ComponentBuilder[] getFormat(Player player) {

        Chat chat = CorePlugin.getChat();
        Permission permission = CorePlugin.getPermissions();

        // Network level
        ComponentBuilder levelComponent = new ComponentBuilder("§71 ");
        levelComponent.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.NETWORK_LEVEL_MSG
                .replace("{0}", "1")).create()));

        // Island level
        long level = asbAPI.getLongIslandLevel(player.getUniqueId());
        ComponentBuilder skyblockLevelComponent = new ComponentBuilder("§8[§6" + level + "§8] ");
        skyblockLevelComponent.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7This user has a SkyBlock\n" +
                "§7level of §6" + level + "§7.").create()));

        // Rank block
        ComponentBuilder rankComponent = new ComponentBuilder(chat.getPlayerPrefix(player).replaceAll("&", "\u00a7"));
        rankComponent.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.USER_BLOCK
                .replace("{0}", permission.getPrimaryGroup(player).toUpperCase()
                        .replaceAll("&", "§"))
                .replace("{1}", "1"))
                .create()));
        rankComponent.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/profile " + player.getName()));

        // Username block
        ComponentBuilder userComponent = new ComponentBuilder(player.getName());
        userComponent.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.USER_BLOCK
                .replace("{0}", permission.getPrimaryGroup(player).toUpperCase()
                        .replaceAll("&", "§"))
                .replace("{1}", "1"))
                .create()));
        userComponent.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/profile " + player.getName()));

        return new ComponentBuilder[]{levelComponent, skyblockLevelComponent, rankComponent, userComponent, new ComponentBuilder("\u00a77: \u00a7r")};
    }
}
