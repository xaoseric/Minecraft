package com.fadelands.core.achievements;

import com.fadelands.core.playerdata.PlayerData;
import org.bukkit.entity.Player;

public class AchievementManager {

    public void giveAchievement(Player player, Achievement achievement) {

    }

    public void removeAchievement(Player player, Achievement achievement) {

    }

    public void resetProgress(Player player, Achievement achievement) {

    }

    public void startAchievement(Player player, Achievement achievement) {
        PlayerData playerData = PlayerData.get(player.getUniqueId());

        if(playerData != null) {
            playerData.getAchivementData().progress(achievement);
        }

    }
}
