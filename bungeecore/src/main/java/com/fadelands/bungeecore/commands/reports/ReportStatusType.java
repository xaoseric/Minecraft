package com.fadelands.bungeecore.commands.reports;

import net.md_5.bungee.api.ChatColor;

public enum ReportStatusType {
    HANDLED(ChatColor.DARK_GREEN, "Handled"),
    CLOSED(ChatColor.RED, "Closed"),
    DENIED(ChatColor.RED, "Denied"),
    OPEN(ChatColor.GREEN, "Open");

    private ChatColor color;
    private String displayMessage;

    ReportStatusType(ChatColor color, String displayMessage) {
        this.color = color;
        this.displayMessage = displayMessage;
    }

    public String toDisplay() {
        return color + displayMessage;
    }
}
