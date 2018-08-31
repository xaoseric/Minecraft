package com.fadelands.core.provider.scoreboard.provider;

import org.bukkit.entity.Player;

import java.util.List;

public interface BoardProvider {

    String getTitle(Player player);

    List<String> getBoardLines(Player player);

    String getNameTag(Player player, Player other);

}
