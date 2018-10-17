package com.fadelands.core.provider.chat;

import com.fadelands.core.Core;
import com.fadelands.core.achievements.Achievement;
import com.fadelands.core.player.UserUtil;
import com.fadelands.core.playerdata.PlayerData;
import com.fadelands.core.utils.TimeUtils;
import com.fadelands.core.utils.UtilTime;
import com.fadelands.core.utils.Utils;
import com.fadelands.core.provider.chat.provider.ChatProvider;
import com.fadelands.core.settings.Settings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@SuppressWarnings("Duplicates")
public class Chat implements Listener {

    private Core plugin;

    private HashMap<UUID, ChatData> lastChatMessage = new HashMap<>();

    private String[] hackusations = {"hacker", "cheater", "cheating", "hacking"};
    private int chatSlow = 0;
    private long silenced;
    private int chatDelay = 3;

    public Chat(Core plugin) {
        this.plugin = plugin;
    }

    public void setChatSlow(int seconds, boolean inform) {
        if (seconds < 0) {
            seconds = 0;
        }

        chatSlow = seconds;

        if (inform) {
            if (seconds == 0) {
                plugin.getServer().broadcastMessage(Utils.Alert + "§2Chat Slow has been disabled.");
            } else {
                plugin.getServer().broadcastMessage(Utils.Alert + "§cChat Slow has been enabled with a " + seconds + " seconds cooldown.");
            }
        }
    }

    public void setSilence(long duration, boolean inform) {
        if (duration > 0) {
            silenced = System.currentTimeMillis() + duration;
        } else {
            silenced = duration;

            if (!inform) {
                return;
            }

            if (duration == -1) {
                plugin.getServer().broadcastMessage(Utils.Alert + "§cThe Chat has been permanently silenced.");
            } else if (duration == 0) {
                plugin.getServer().broadcastMessage(Utils.Alert + "§2The Chat is no longer silenced.");
            } else {
                plugin.getServer().broadcastMessage(Utils.Alert + "§cThe Chat has been silenced for " + TimeUtils.toRelative(duration) + ".");
            }
        }
    }

    public void silenceEnd() {
        if (silenced <= 0) {
            return;
        }

        if (System.currentTimeMillis() > silenced) {
            setSilence(0, true);
        }
    }

    private boolean checkSilenced(Player player) {

        silenceEnd();

        if (silenced == 0) {
            return false;
        }

        if (UserUtil.isStaff(player.getName())) {
            return false;
        }

        if (silenced == -1) {
            player.sendMessage(Utils.Alert + "§cChat is silenced permanently.");
            return true;
        } else {
            player.sendMessage(Utils.Alert + "§cChat is silenced for " + TimeUtils.toRelative(silenced) + ".");
            return true;
        }
    }

    public boolean chatDelayCheck(Player player) {
        if (UserUtil.isStaff(player.getName())) {
            return false;
        }

        if(silenced == -1) {
            player.sendMessage(Utils.Alert + "§cChat is silenced permanently.");
        } else {
            player.sendMessage(Utils.Alert + "§cChat is silenced for " + TimeUtils.toRelative(silenced) + ".");
        }
        return true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleChat(AsyncPlayerChatEvent event) {
        if(event.isCancelled()) {
            return;
        }

        event.setCancelled(true);

        String message = event.getMessage();

        Settings settings = new Settings();

        Player chatSender = event.getPlayer();

        if (!settings.publicChat(chatSender)) {
            chatSender.sendMessage(Utils.Alert + "§cYou have toggled public chat off! To chat with others, change your settings with /settings.");
            return;
        }

        if (checkSilenced(chatSender)) {
            return;

        }

        if (lastChatMessage.containsKey(chatSender.getUniqueId())) {
            ChatData lastMessage = lastChatMessage.get(chatSender.getUniqueId());

            long remaining = chatDelay - lastMessage.getTimeSent();
            if (remaining > 0 && !UserUtil.isStaff(chatSender.getName())) {
                chatSender.sendMessage(Utils.Alert + "§2Please wait " + (remaining / 1000) + " seconds before each message.");
                return;
            }

            long chatSlowTime = 1000L * chatSlow;
            long timeDiff = System.currentTimeMillis() - lastMessage.getTimeSent();
            if (timeDiff < chatSlowTime && !UserUtil.isStaff(chatSender.getName())) {
                chatSender.sendMessage(Utils.Alert + "§2Chat Slow is currently enabled, please wait " + UtilTime.MakeStr(chatSlowTime - timeDiff) + ".");
                return;
            }

            for (String s : getHackusations()) {
                if (message.contains(s)) {
                    chatSender.sendMessage(Utils.Alert + "§aInstead of hackusating in chat, please report the user with /report." +
                            " Hackusating another player can make them turn off their hacks before a staff member can punish them.");
                    return;
                }
            }
        }

        ChatProvider chatProvider = plugin.getChatProvider();

        ComponentBuilder[] format = chatProvider.getFormat(chatSender);
        List<BaseComponent> components = new ArrayList<>();

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < format.length; i++) {
            components.addAll(Arrays.asList(format[i].create()));
        }

        components.add(new TextComponent(message));

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (settings.publicChat(online)) {
                //noinspection ToArrayCallWithZeroLengthArrayArgument
                online.spigot().sendMessage(components.toArray(new BaseComponent[components.size()]));

            }

            lastChatMessage.put(chatSender.getUniqueId(), new ChatData(event.getMessage()));
            PlayerData playerData = PlayerData.get(chatSender.getUniqueId());
            if (playerData != null) {
                playerData.getStats().setMessagesSent(playerData.getStats().getMessagesSent() + 1);
            }
            plugin.getAchManager().startAchievement(chatSender, Achievement.FIRST_WORDS);

            try(Connection connection = plugin.getDatabaseManager().getConnection()){
                try(PreparedStatement insert = connection
                        .prepareStatement("INSERT INTO chat_messages (player_uuid,player_username,server,date,messages) VALUE (?,?,?,?,?)")) {

                    insert.setString(1, chatSender.getUniqueId().toString());
                    insert.setString(2, chatSender.getName());
                    insert.setString(3, plugin.getPluginMessage().getServerName(chatSender));
                    insert.setTimestamp(4, new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
                    insert.setString(5, message);
                    insert.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String[] getHackusations() {
        return this.hackusations;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastChatMessage.remove(event.getPlayer().getUniqueId());
    }

    public void setChatDelay(int number) {
        chatDelay = number;
    }

}
