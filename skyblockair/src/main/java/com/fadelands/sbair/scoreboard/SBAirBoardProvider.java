package com.fadelands.sbair.scoreboard;

import com.fadelands.array.utils.PluginMessage;
import com.fadelands.core.provider.scoreboard.SimpleBoardProvider;
import com.fadelands.sbair.Main;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;

import java.util.List;

import static com.fadelands.sbair.Main.asbAPI;

public class SBAirBoardProvider extends SimpleBoardProvider {

    private PluginMessage pluginMessage;

    public SBAirBoardProvider(PluginMessage pluginMessage) {
        this.pluginMessage = pluginMessage;
    }

    @Override
    public String getTitle(Player player) {
        return "&f&lFade&b&lLands";
    }

    @Override
    public List<String> getBoardLines(Player player) {
        List<String> toReturn = Lists.newArrayList();

        int total_members = asbAPI.getTeamMembers(player.getUniqueId()).size();


            asbAPI.getTeamMembers(player.getUniqueId()).add(player.getUniqueId());

            toReturn.add("&r ");
            toReturn.add("&f&l\u00bb &f&l" + player.getName());
            toReturn.add("&7Rank&f: " + Main.getPermissions().getPrimaryGroup(player));
            toReturn.add("&7Tokens&f: &30");
            toReturn.add("&r ");
            toReturn.add("&f&l\u00bb Island"); // <3
            toReturn.add("&7Level&f: &3" + asbAPI.getLongIslandLevel(player.getUniqueId()));
            toReturn.add("&7Team&f: &3" + asbAPI.getTeamMembers(player.getUniqueId()).size());
            toReturn.add("&f&owww.fadelands.com");
        return toReturn;
    }

    @Override
    public String getNameTag(Player player, Player other) {
        return super.getNameTag(player, other); // Exists in core already, use this if you dont want to override <-

    }

}

