package com.fadelands.core.provider.scoreboard;

import com.fadelands.core.CorePlugin;
import com.fadelands.core.provider.scoreboard.provider.BoardProvider;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SimpleboardManager extends BukkitRunnable implements Listener {

    private Map<UUID, Simpleboard> boards;
    private BoardProvider boardProvider;

    public SimpleboardManager(CorePlugin plugin, BoardProvider boardProvider) {
        this.boardProvider = boardProvider;
        this.boards = Maps.newHashMap();
    }// SKIFT + F6 för att byta namn (exempel)
    // SKIFT + F6 två gånger för att byta i hela project


    public BoardProvider getBoardProvider() {
        return boardProvider;
    }

    public void setBoardProvider(BoardProvider boardProvider) {
        this.boardProvider = boardProvider;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Simpleboard simpleboard = boards.get(event.getPlayer().getUniqueId());

        if (simpleboard == null) {
            simpleboard = new Simpleboard(event.getPlayer());
            boards.put(player.getUniqueId(), simpleboard);
        }

        simpleboard.updateTitle(boardProvider.getTitle(player));
        simpleboard.show();
        // "mvn clean install -pl lobby -am"
        // -pl specifierar vilken module
        // -am betyder "also make" vilket menar att den kommer builda dependencies som finns i samma project alltså array
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        boards.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent event) {
        boards.remove(event.getPlayer().getUniqueId());
    }

    @Override
    public void run() {
        ImmutableSet.copyOf(Bukkit.getOnlinePlayers()).forEach(player -> {
            Simpleboard simpleboard = boards.get(player.getUniqueId());

            if (simpleboard == null) {
                return;
            }

            List<String> list = boardProvider.getBoardLines(player);
            simpleboard.update(list);
            simpleboard.updateTitle(boardProvider.getTitle(player));

            ImmutableSet.copyOf(Bukkit.getOnlinePlayers()).forEach(other -> {
                simpleboard.updateNameTag(other, boardProvider.getNameTag(player, other));
            });
        });
    }
}
