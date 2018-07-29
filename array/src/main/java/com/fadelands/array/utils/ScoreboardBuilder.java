package com.fadelands.array.utils;

import com.avaje.ebeaninternal.server.lib.util.NotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.util.HashSet;
import java.util.Set;

public class ScoreboardBuilder {

    private Objective objective;
    private Scoreboard scoreboard;
    private Set<String> entryNames;
    private int score = 20;
    /**
     * @author RealPurox [Daniel]
     * This class is created by RealPurox. Credit for this
     * scoreboard builder goes to him.
     */
    /**
     *
     * @param name name used to identify the scoreboard
     * @param displayName scoreboard's display name
     * @param displaySlot scoreboard's display slot
     */
    public ScoreboardBuilder(String name, String displayName, DisplaySlot displaySlot) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(name, "dummy");
        objective.setDisplaySlot(displaySlot);
        objective.setDisplayName(displayName);

        this.entryNames = new HashSet<>();
        this.scoreboard = scoreboard;
        this.objective = objective;
    }

    /**
     *
     * @return the objective
     */
    public Objective getObjective() {
        return objective;
    }

    /**
     *
     * @return the scoreboard
     */
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    /**
     *
     * @param name name used to identify the scoreboard
     * @param prefix team prefix, can be updated, max 16 characters
     * @param entry team entry, max 16 characters, cannot be used in multiple teams, cannot be updated,
     * @param suffix team suffix, can be updated, max 16 characters
     * @param score team score
     * @throws IllegalArgumentException if prefix, entry or name is longer than 16 characters or entry is already used in another Team
     */
    public Team createTeam(String name, String prefix, String entry, String suffix, int score) {
        if (entryNames.contains(entry))
            throw new IllegalArgumentException("An entry with the name '" + entry + "' already exists.");

        if (prefix.length() > 16)
            throw new IllegalArgumentException("prefix cannot be longer than 16 characters");
        if (entry.length() > 16)
            throw new IllegalArgumentException("entry cannot be longer than 16 characters");
        if (suffix.length() > 16)
            throw new IllegalArgumentException("suffix cannot be longer than 16 characters");

        Team team = scoreboard.registerNewTeam(name);

        team.setPrefix(prefix);
        team.addEntry(entry);
        team.setSuffix(suffix);

        objective.getScore(entry).setScore(score);

        entryNames.add(entry);
        return team;
    }

    /**
     *
     * @param name name used to identify the scoreboard
     * @param prefix team prefix, can be updated, max 16 characters
     * @param entry team entry, max 16 characters, cannot be used in multiple teams, cannot be updated,
     * @param suffix team suffix, can be updated, max 16 characters
     * @throws IllegalArgumentException if prefix, entry or name is longer than 16 characters or entry is already used in another Team
     */
    public Team createTeam(String name, String prefix, String entry, String suffix) {
        return createTeam(name, prefix, entry, suffix, score--);
    }

    /**
     *
     * @param team the team you want to update
     * @param newSuffix new suffix for the team
     */
    public void updateSuffix(Team team, String newSuffix) {
        team.setSuffix(newSuffix);
    }

    /**
     *
     * @param teamName the name of the team you want to update
     * @param newSuffix new suffix for the team
     * @throws NotFoundException if the team wasn't found
     */
    public void updateSuffix(String teamName, String newSuffix) {
        Team team = scoreboard.getTeam(teamName);
        if (team == null)
            throw new NotFoundException("Scoreboard Team with name " + teamName + " was not found.");
        team.setSuffix(newSuffix);
    }

    public void updateTitle(String newTitle) {
        objective.setDisplayName(newTitle);
    }

    /**
     *
     * @param teamName the name of the team you want to update
     * @param newPrefix new prefix for the team
     * @throws NotFoundException if the team wasn't found
     */
    public void updatePrefix(String teamName, String newPrefix) {
        Team team = scoreboard.getTeam(teamName);
        if (team == null)
            throw new NotFoundException("Scoreboard Team with name " + teamName + " was not found.");
        team.setPrefix(newPrefix);
    }

    /**
     *
     * @param team the team you want to update
     * @param newPrefix new prefix for the team
     */
    public void updatePrefix(Team team, String newPrefix) {
        team.setSuffix(newPrefix);
    }
}
