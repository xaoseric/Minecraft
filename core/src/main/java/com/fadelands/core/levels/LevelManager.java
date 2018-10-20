package com.fadelands.core.levels;

public class LevelManager {

    public int getRequiredExp(int level) {
        return (25*level*(level+1));
    }

    public int getLevel(int exp) {
        int level = 1;
        while (exp > getRequiredExp(level)) level++;
        return level - 1;
    }

    public int getExpTillNextLevel(int exp) {
        return getRequiredExp(getLevel(exp) + 1);
    }

    public String getProgressBar(int exp) {
        double percent = (double) exp / (double) getExpTillNextLevel(exp);
        int lines = (int) (percent * 30);

        StringBuilder bar = new StringBuilder("§7[§a");
        for (int i = 0; i < 30; i++) {
            if (i == lines) bar.append("§7");
            bar.append("|");
        }

        bar.append("§7] ").append(Math.round(percent * 100)).append("%");
        return bar.toString();
    }

}
