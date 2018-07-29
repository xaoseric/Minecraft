package com.fadelands.lobby.scoreboard;

import com.fadelands.array.plmessaging.PluginMessage;
import com.fadelands.core.scoreboard.SimpleBoardProvider;
import com.fadelands.lobby.Main;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;

import javax.annotation.processing.SupportedAnnotationTypes;
import java.util.List;

@SuppressWarnings("ALL")
public class LobbyBoardProvider extends SimpleBoardProvider {

    private PluginMessage pluginMessage;
    private int LOBBY_PLAYERCOUNT;
    private int SB_AIR_PLAYERCOUNT;
    private int SB_WATER_PLAYERCOUNT;
    private int SV_EARTH_PLAYERCOUNT;

    private int SB_REALMS_PLAYERCOUNT;
    private int SV_REALMS_PLAYERCOUNT;

    public LobbyBoardProvider(PluginMessage pluginMessage) {
        this.pluginMessage = pluginMessage;
    }


    @Override
    public String getTitle(Player player) {
        return "&f&lFade&b&lLands";
    }

    @Override
    public List<String> getBoardLines(Player player) {
        List<String> toReturn = Lists.newArrayList();

        if (pluginMessage == null) {
            return null;
        }

        this.LOBBY_PLAYERCOUNT = pluginMessage.getPlayers("LOBBY");
        this.SB_AIR_PLAYERCOUNT = pluginMessage.getPlayers("SB-AIR");
        this.SB_WATER_PLAYERCOUNT = pluginMessage.getPlayers("SB-WATER");
        this.SV_EARTH_PLAYERCOUNT = pluginMessage.getPlayers("SV-EARTH");

        toReturn.add("&r ");
        toReturn.add("&f&l\u00bb &f&l" + player.getName());
        toReturn.add("&7Rank&f: " + Main.getPermissions().getPrimaryGroup(player));
        toReturn.add("&7Tokens&f: &30");
        toReturn.add("&r ");
        toReturn.add("&f&l\u00bb Servers"); // <3
        toReturn.add("&8&l┏━");
        toReturn.add("&8&l┣ &7Water&f: &3" + SB_WATER_PLAYERCOUNT);
        toReturn.add("&8&l┣ &7Air&f: &3" + SB_AIR_PLAYERCOUNT);
        toReturn.add("&8&l┣ &7Earth&f: &3" + SV_EARTH_PLAYERCOUNT);
        toReturn.add("&8&l┣ &7Lobby&f: &3" + LOBBY_PLAYERCOUNT);
        toReturn.add("&8&l┗━");
        toReturn.add("&f&owww.fadelands.com");

        return toReturn;
    }

    @Override
    public String getNameTag(Player player, Player other) {
        return super.getNameTag(player, other); // Exists in core already, use this if you dont want to override <-

    }

}
