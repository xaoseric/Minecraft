package com.fadelands.lobby.scoreboard;

import com.fadelands.array.utils.PluginMessage;
import com.fadelands.core.provider.scoreboard.SimpleBoardProvider;
import com.fadelands.lobby.Main;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("ALL")
public class LobbyBoardProvider extends SimpleBoardProvider {

    private PluginMessage pluginMessage;
    private int SERVER_COUNT;

    public LobbyBoardProvider(PluginMessage pluginMessage) {
        this.pluginMessage = pluginMessage;
    }


    @Override
    public String getTitle(Player player) {
        return "&f&lFade&6&lLands";
    }

    @Override
    public List<String> getBoardLines(Player player) {
        List<String> toReturn = Lists.newArrayList();

        if (pluginMessage == null) {
            return null;
        }

        this.SERVER_COUNT = pluginMessage.getPlayers("ALL");

        toReturn.add("&r ");
        toReturn.add("&7&l\u00bb &f&l" + player.getName());
        toReturn.add(" &7Tokens &21");
        toReturn.add(" &7Points &21");
        toReturn.add("&r ");
        toReturn.add("&7&l\u00bb &f&lRank");
        toReturn.add(" " + Main.getPermissions().getPrimaryGroup(player));
        toReturn.add("&r ");
        toReturn.add("&7&l\u00bb &f&lLobby");
        toReturn.add(" &6" + pluginMessage.getServerName(player));
        toReturn.add("&r ");
        toReturn.add("&7&l\u00bb &f&lPlayers");
        toReturn.add(" §6" + SERVER_COUNT);
        toReturn.add("&r ");
        toReturn.add("&2www.fadelands.com");

        return toReturn;
    }

    @Override
    public String getNameTag(Player player, Player other) {
        return super.getNameTag(player, other); // Exists in core already, use this if you dont want to override <-

    }

}
