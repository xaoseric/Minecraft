package com.fadelands.core.scoreboard;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.awt.*;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Simpleboard {

    private Player player;
    private org.bukkit.scoreboard.Scoreboard scoreboard;
    private Objective objective;
    private List<String> oldLines;
    private boolean hidden;

    public Simpleboard(Player player) {
        this.player = player;
        this.scoreboard = player.getScoreboard();

        if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        this.objective = scoreboard.getObjective("fadelands<3");

        if (objective == null) {
            objective = scoreboard.registerNewObjective("fadelands<3", "dummy");
        }

        IntStream.range(0, 16).forEach(i -> {
            Team team = scoreboard.getTeam("team-" + getTeamName(i));
            if (team == null) {
                team = scoreboard.registerNewTeam("team-" + getTeamName(i));
            }
        });
        this.oldLines = Lists.newArrayList();
        this.player.setScoreboard(scoreboard);
    }

    public void updateTitle(String value) {
        value = ChatColor.translateAlternateColorCodes('&', value);
        objective.setDisplayName(value);
    }

    public void updateLine(int lineNumber, String value) {
        value = ChatColor.translateAlternateColorCodes('&', value);

        String[] prefixAndSuffix = getPrefixAndSuffix(value);

        Team team = scoreboard.getTeam("team-" + getTeamName(lineNumber));
        team.setPrefix(prefixAndSuffix[0]);
        team.setSuffix(prefixAndSuffix[1]);

        if (!team.getEntries().contains(getTeamName(lineNumber))) {
            team.addEntry(getTeamName(lineNumber));
        }

        objective.getScore(getTeamName(lineNumber)).setScore(lineNumber);
    }

    public void hide() {
        hidden = true;
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
    }

    public void show() {
        hidden = false;
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void updateNameTag(Player other, String value) {
        value = ChatColor.translateAlternateColorCodes('&', value);

        Team team = scoreboard.getTeam(other.getName());
        if (team == null) {
            team = scoreboard.registerNewTeam(other.getName());
        }

        String prefix = "";
        String suffix = "";

        try {prefix = value.split("\\{")[0];} catch (Exception ignored) {}
        try {suffix = value.split("}")[1];} catch (Exception ignored) {}

        if (prefix.length() > 16) {
            prefix = prefix.substring(0, 16);
        }

        if (suffix.length() > 16) {
            suffix = suffix.substring(0, 16);
        }
        team.setPrefix(prefix);
        team.setSuffix(suffix);

        if (!team.getEntries().contains(other.getName())) {
            team.addEntry(other.getName());
        }
    }

    public void removeLine(int lineNumber) {
        scoreboard.resetScores(getTeamName(lineNumber));
    }

    public void update(List<String> list) {
        if (hidden) {
            return;
        }

        if (list == null) {
            return;
        }

        if (player == null) {
            return;
        }

        while (list.size() > 15) {
            list.remove(list.size() - 1);
        }

        int slot = list.size();

        if (slot < 15) {
            for (int i = oldLines.size(); i > list.size(); i--) {
                removeLine(i);
            }
        }

        for (String value : list) {
            updateLine(slot, value);
            slot--;
        }

        oldLines = list;
    }

    private String[] getPrefixAndSuffix(String value) {
        String prefix = getPrefix(value);
        String suffix = getPrefix(ChatColor.getLastColors(prefix) + getSuffix(value));

        return new String[]{prefix, suffix};
    }

    private String getPrefix(String value) {
        return value.length() > 16 ? value.substring(0, 16) : value;
    }

    private String getSuffix(String value) {
        value = value.length() > 32 ? value.substring(0, 32) : value;

        return value.length() > 16 ? value.substring(16) : "";
    }

    private String getTeamName(int lineNumber) {
        return ChatColor.values()[lineNumber].toString();
    }
}
