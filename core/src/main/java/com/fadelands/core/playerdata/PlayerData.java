package com.fadelands.core.playerdata;

import com.fadelands.core.achievement.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerData {

    private static final List<PlayerData> all = new ArrayList<>();

    private final UUID uuid;
    private final Statistics stats;
    private final AchievementData achData;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.stats = new Statistics();
        this.achData = new AchievementData();

        all.add(this);
    }

    public void remove() {
        all.remove(this);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Statistics getStats() {
        return this.stats;
    }

    public AchievementData getAhievementData() {
        return this.achData;
    }

    public static List<PlayerData> getAll() {
        return all;
    }

    public static PlayerData get(UUID uuid) {
        for (int i = 0; i < all.size(); i++)
            if (all.get(i).getUUID() == uuid) return all.get(i);
        return null;
    }

    public final class Statistics {

        private int tokens = 0, messagesSent = 0, commandsUsed = 0, loginCount = 0, blocksPlaced = 0, blocksRemoved = 0, deaths = 0, kills = 0;

        public int getTokens() {
            return tokens;
        }

        public void setTokens(int tokens) {
            this.tokens = tokens;
        }

        public int getMessagesSent() {
            return messagesSent;
        }

        public void setMessagesSent(int messagesSent) {
            this.messagesSent = messagesSent;
        }

        public int getCommandsUsed() {
            return commandsUsed;
        }

        public void setCommandsUsed(int commandsUsed) {
            this.commandsUsed = commandsUsed;
        }

        public int getLoginCount() {
            return loginCount;
        }

        public void setLoginCount(int loginCount) {
            this.loginCount = loginCount;
        }

        public int getBlocksPlaced() {
            return blocksPlaced;
        }

        public void setBlocksPlaced(int blocksPlaced) {
            this.blocksPlaced = blocksPlaced;
        }

        public int getBlocksRemoved() {
            return blocksRemoved;
        }

        public void setBlocksRemoved(int blocksRemoved) {
            this.blocksRemoved = blocksRemoved;
        }

        public int getDeaths() {
            return deaths;
        }

        public void setDeaths(int deaths) {
            this.deaths = deaths;
        }

        public int getKills() {
            return kills;
        }

        public void setKills(int kills) {
            this.kills = kills;
        }

    }


    public final class AchievementData {

        private final List<Achievement> achieved = new ArrayList<>();
        private final Map<Achievement, Integer> progress = new HashMap<>();

        public List<Achievement> getAchieved() {
            return achieved;
        }

        public Map<Achievement, Integer> getProgressData() {
            return this.progress;
        }

        public void progress(Achievement ach) {

            if (this.achieved.contains(ach)) return;

            if (ach.getRequirement() > 0) {
                int progress = getProgress(ach);

                if (!(++progress >= ach.getRequirement())) {
                    this.progress.put(ach, progress);
                    return;
                }

                this.progress.remove(ach);
            }

            achieved.add(ach);

            Player player = Bukkit.getPlayer(PlayerData.this.uuid);

            player.sendMessage("§r");
            player.sendMessage("§8§l┏━━ §6§kIII§e§l Achievement Unlocked §6§k III§8§l ━━┓ ");
            player.sendMessage(" §6§lName");
            player.sendMessage(" §7" + ach.getDisplayName() + " (" + ach.getDifficulty().getDisplay() + "§7)");
            player.sendMessage(" §6§lDescription");
            player.sendMessage(" §7" + ach.getDescription().get(0) + ".");
            player.sendMessage("§8§l┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
            player.sendMessage("§r");

            //Idk, maybe send some kind of message to the player, like "Achievement Complete!"
        }

        public int getProgress(Achievement ach) {
            if (ach.getRequirement() <= 0) return 0;
            return this.progress.getOrDefault(ach, 0);
        }

        //Osäker ifall detta fungerar korrekt då casts kan fucka ibland
        public int getProcentProgress(Achievement ach) {
            return (int)((double)(getProgress(ach)/ach.getRequirement()) * 100);
        }
    }
}


