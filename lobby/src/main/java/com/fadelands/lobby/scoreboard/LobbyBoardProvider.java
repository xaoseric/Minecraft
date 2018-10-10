package com.fadelands.lobby.scoreboard;

import com.fadelands.array.utils.DateUtils;
import com.fadelands.array.utils.PluginMessage;
import com.fadelands.core.provider.scoreboard.SimpleBoardProvider;
import com.fadelands.lobby.Main;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.Date;
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

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        toReturn.add("§7      " + getMonthName(cal.get(Calendar.MONTH)) + " " + DateUtils.getFormattedDayDate(cal.getTime()));
        toReturn.add("§r");
        toReturn.add("§6§l\u00bb " + player.getName());
        toReturn.add("  §fRank: " + Main.getPermissions().getPrimaryGroup(player));
        toReturn.add("  §fServer: §a" + pluginMessage.getServerName(player));
        toReturn.add("§r");
        toReturn.add("§6§l┏━ Players");
        toReturn.add("§6§l┣ &fSkyBlock: §a§l" + (pluginMessage.getPlayers("SB-AIR") + pluginMessage.getPlayers("SB-WATER")));
        toReturn.add("§6§l┣ &fSurvival: §a§l" + (pluginMessage.getPlayers("SV-EARTH")));
        toReturn.add("§6§l┗━ ");
        toReturn.add("§e§nwww.fadelands.com");

        return toReturn;
    }

    @Override
    public String getNameTag(Player player, Player other) {
        return super.getNameTag(player, other); // Exists in core already, use this if you dont want to override <-
    }

    public static String getMonthName(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }
}
