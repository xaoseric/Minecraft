package com.fadelands.sbair.provider.chat;

import com.fadelands.core.playerdata.PlayerData;
import com.fadelands.core.utils.Utils;
import com.fadelands.sbair.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;

@SuppressWarnings("Duplicates")
public class SBChatProvider implements com.fadelands.core.provider.chat.provider.ChatProvider {

    private Main main;

    public SBChatProvider(Main main) {
        this.main = main;
    }

    @Override
    public ComponentBuilder[] getFormat(Player player) {

        Chat chat = main.getChat();
        Permission permission = main.getPermissions();

        PlayerData playerData = PlayerData.get(player.getUniqueId());
        assert playerData != null;
        PlayerData.Statistics playerStats = playerData.getStats();

        // Network Level
        ComponentBuilder levelComponent = new ComponentBuilder("§7" +  playerStats.getNetworkLevel() + " ");
        levelComponent.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.NETWORK_LEVEL_MSG
                .replace("{0}", String.valueOf(playerStats.getNetworkLevel()))).create()));


        // Island level
        long level = main.getSkyBlockApi().getLongIslandLevel(player.getUniqueId());
        ComponentBuilder skyblockLevelComponent = new ComponentBuilder("§8[§6" + level + "§8] ");
        skyblockLevelComponent.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7This user has a SkyBlock\n" +
                "§7level of §6" + level + "§7.").create()));

        // Rank
        ComponentBuilder rankComponent = new ComponentBuilder(chat.getPlayerPrefix(player).replaceAll("&", "\u00a7"));
        rankComponent.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.USER_BLOCK
                .replace("{0}", permission.getPrimaryGroup(player)
                        .replaceAll("&", "§"))).create()));
        rankComponent.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/profile " + player.getName()));

        // Username
        ComponentBuilder userComponent = new ComponentBuilder("§7" + player.getName());
        userComponent.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.USER_BLOCK
                .replace("{0}", permission.getPrimaryGroup(player)
                        .replaceAll("&", "§"))).create()));
        userComponent.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/profile " + player.getName()));


        return new ComponentBuilder[]{levelComponent, skyblockLevelComponent, rankComponent, userComponent, new ComponentBuilder("\u00a77: \u00a7r")};
    }
}
