package com.fadelands.sbair.scoreboard;

import com.fadelands.array.utils.PluginMessage;
import com.fadelands.core.provider.scoreboard.SimpleBoardProvider;
import com.fadelands.sbair.Main;
import com.fadelands.sbair.utils.DateUtils;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SBBoardProvider extends SimpleBoardProvider {

    private PluginMessage pluginMessage;
    private Main main;

    public SBBoardProvider(PluginMessage pluginMessage, Main main) {
        this.pluginMessage = pluginMessage;
        this.main = main;

    }

    @Override
    public String getTitle(Player player) {
        return "&f&lFade&6&lLands";
    }

    @Override
    public List<String> getBoardLines(Player player) {
        List<String> toReturn = Lists.newArrayList();

        Date date = new Date();
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        for(int i=0;i<32;i++){
        }

            toReturn.add("&7        " + getMonthName(cal.get(Calendar.MONTH)) + " " + DateUtils.getFormattedDayDate(cal.getTime()));
            toReturn.add("&r");
            toReturn.add("&6&lYou");
            toReturn.add("  &7Rank: " + main.getPermissions().getPrimaryGroup(player));
            toReturn.add("  &7Money: &2$" + main.getEconomy().getBalance(player.getName() + "k"));
            toReturn.add("&r ");
            toReturn.add("&6&lIsland");
            toReturn.add("  &7Level: &f" + main.getSkyBlockApi().getLongIslandLevel(player.getUniqueId()));
            toReturn.add("  &7Team: §f" + main.getSkyBlockApi().getTeamMembers(player.getUniqueId()).size());
            toReturn.add("&r ");
            toReturn.add("&ewww.fadelands.com");

        cal.set(Calendar.DATE,(cal.getTime().getDate()+1));

            return toReturn;
    }
        /*
        Old scoreboard:
            toReturn.add("&r ");
            toReturn.add("&7&l\u00bb &f&l" + player.getName());
            toReturn.add("&7Rank: " + main.getPermissions().getPrimaryGroup(player));
            toReturn.add("&7Money: &2" + main.getEconomy().getBalance(player.getName() + "k"));
            toReturn.add("&r ");
            toReturn.add("&7&l\u00bb &f&lIsland");
            toReturn.add("&7Level: &2" + main.getSkyBlockApi().getLongIslandLevel(player.getUniqueId()));
            toReturn.add("&7Team: §2" + main.getSkyBlockApi().getTeamMembers(player.getUniqueId()).size());
            toReturn.add("&r ");
            toReturn.add("&2www.fadelands.com");
        return toReturn;
         */

    @Override
    public String getNameTag(Player player, Player other) {
        return super.getNameTag(player, other); // Exists in core already, use this if you dont want to override <-

    }

    public static String getMonthName(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }

}

