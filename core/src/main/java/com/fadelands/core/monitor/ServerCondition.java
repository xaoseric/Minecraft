package com.fadelands.core.monitor;

import org.bukkit.ChatColor;

public enum ServerCondition {

    GOOD(ChatColor.GREEN, "Good"),
    CRITICAL(ChatColor.GOLD, "Critical"),
    BAD(ChatColor.RED, "Bad");

    private ChatColor color;
    private String condition;

    ServerCondition(ChatColor color, String condition) {
        this.color = color;
        this.condition = condition;
        }

        public String getCondition() {
            return color + condition;
        }

    }
