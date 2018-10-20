package com.fadelands.core.playerdata;

import com.fadelands.core.achievements.Achievement;
import com.fadelands.core.punishments.Punishment;
import com.fadelands.core.punishments.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class PlayerData {

    private static final HashMap<UUID, PlayerData> all = new HashMap<>();

    private final UUID uuid;
    private final Statistics stats;
    private final AchievementData achData;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.stats = new Statistics();
        this.achData = new AchievementData();

        all.put(uuid, this);
    }

    public void remove(UUID uuid) {
        all.remove(uuid, this);
    }

    UUID getUUID() {
        return this.uuid;
    }

    public Statistics getStats() {
        return this.stats;
    }

    public AchievementData getAchivementData() {
        return this.achData;
    }

    public static HashMap<UUID, PlayerData> getAll() {
        return all;
    }

    public static PlayerData get(UUID uuid) {
        return all.get(uuid);
    }

    public final class Statistics {

        private int exp = 0, points = 0, tokens = 0, messagesSent = 0, commandsUsed = 0, loginCount = 0, blocksPlaced = 0, blocksRemoved = 0, playtime = 0, deaths = 0, kills = 0;

        public int getExp() {
            return exp;
        }

        public void setExp(int exp) {
            this.exp = exp;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

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

        public int getPlaytime() {
            return playtime;
        }

        public void setPlaytime(int minutes) {
            this.playtime = minutes;
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

        public List<Achievement> getAll() {
            return Achievement.getAll();
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
            player.sendMessage(" §6§lTokens");
            player.sendMessage(" §2+" + ach.getDifficulty().getTokens());
            player.sendMessage("§8§l┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
            player.sendMessage("§r");

            PlayerData playerData = PlayerData.get(player.getUniqueId());
            if (playerData != null) {
                playerData.getStats().setTokens(playerData.getStats().getTokens() + ach.getDifficulty().getTokens()); //add tokens to playerdata <-
            }
        }

        public int getProgress(Achievement ach) {
            if (ach.getRequirement() <= 0) return 0;
            return this.progress.getOrDefault(ach, 0);
        }
        /*
        public int getProcentProgress(Achievement ach) {
            return (int)((double)(getProgress(ach)/ach.getRequirement()) * 100);
        }*/

        public boolean hasCompleted(Achievement ach) {
            return achieved.contains(ach);
        }

        public boolean hasStarted(Achievement ach) {
            return progress.containsKey(ach);
        }
    }
}

