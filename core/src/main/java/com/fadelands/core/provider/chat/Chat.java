package com.fadelands.core.provider.chat;

import com.fadelands.core.Core;
import com.fadelands.core.player.User;
import com.fadelands.core.playerdata.PlayerData;
import com.fadelands.core.utils.TimeUtils;
import com.fadelands.core.utils.Utils;
import com.fadelands.core.provider.chat.provider.ChatProvider;
import com.fadelands.core.settings.Settings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        if (seconds < 0)
            seconds = 0;

        chatSlow = seconds;

        if (inform) {
            if (seconds == 0)
                plugin.getServer().broadcastMessage(Utils.Alert + "§2Chat Slow has been disabled.");
            else
                plugin.getServer().broadcastMessage(Utils.Alert + "§cChat Slow has been enabled with a " + seconds + " seconds cooldown.");
        }
    }

    public void setSilence(long duration, boolean inform) {
        if (duration > 0)
            silenced = System.currentTimeMillis() + duration;
        else
            silenced = duration;

        if(!inform)
            return;

        if (duration == -1)
            plugin.getServer().broadcastMessage(Utils.Alert + "§cThe Chat has been permanently silenced.");
        else if (duration == 0)
            plugin.getServer().broadcastMessage(Utils.Alert + "§2The Chat is no longer silenced.");
        else
            plugin.getServer().broadcastMessage(Utils.Alert + "§cThe Chat has been silenced for " + TimeUtils.toRelative(duration) + ".");
    }

    public void silenceEnd() {
        if(silenced <= 0)
            return;
        if (System.currentTimeMillis() > silenced)
            setSilence(0, true);
    }

    private boolean checkSilenced(Player player) {

        silenceEnd();

        if (silenced == 0)
            return false;
        if (new User().isStaff(player.getName()))
            return false;
        if(silenced == -1)
            player.sendMessage(Utils.Alert + "§cChat is silenced permanently.");
        else
            player.sendMessage(Utils.Alert + "§cChat is silenced for " + TimeUtils.toRelative(silenced) + ".");
        return true;
    }

    public boolean chatDelayCheck(Player player) {
        if (new User().isStaff(player.getName()))
            return false;
        if(silenced == -1)
            player.sendMessage(Utils.Alert + "§cChat is silenced permanently.");
        else
            player.sendMessage(Utils.Alert + "§cChat is silenced for " + TimeUtils.toRelative(silenced) + ".");
        return true;
    }

    @EventHandler
    public void handleChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled())
            return;

        event.setCancelled(true);
        Player chatSender = event.getPlayer();

        Settings settings = new Settings();
        User user = new User();

        if (!settings.publicChat(chatSender)) {
            chatSender.sendMessage(Utils.Prefix + "§cYou have toggled public chat off! To chat with others, change your settings with /settings.");
            return;
        }

        if (checkSilenced(chatSender)) {
            return;

        } else if (lastChatMessage.containsKey(chatSender.getUniqueId())) {
            ChatData lastMessage = lastChatMessage.get(chatSender.getUniqueId());

            /*long remaining = chatDelay - lastMessage.getTimeSent();
            if (remaining > 0 && !user.isStaff(chatSender.getName())) {
                chatSender.sendMessage(Utils.Alert + "§2Please wait " + (remaining / 1000) + " seconds before each message.");
                return;
            }*/

            long chatSlowTime = 1000L * chatSlow;
            long timeDiff = System.currentTimeMillis() - lastMessage.getTimeSent();
            if (timeDiff < chatSlowTime && !user.isStaff(chatSender.getName())) {
                chatSender.sendMessage(Utils.Alert + "§2Chat Slow is currently enabled, please wait " + TimeUtils.toRelative(chatSlowTime - timeDiff) + ".");
                return;
            }

            for (String s : getHackusations()) {
                if (event.getMessage().contains(s)) {
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

        components.add(new TextComponent(event.getMessage()));

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (settings.publicChat(online)) {
                //noinspection ToArrayCallWithZeroLengthArrayArgument
                online.spigot().sendMessage(components.toArray(new BaseComponent[components.size()]));

            }
            lastChatMessage.put(chatSender.getUniqueId(), new ChatData(event.getMessage()));
            PlayerData.Statistics playerData = Objects.requireNonNull(PlayerData.get(chatSender.getUniqueId())).getStats();
            playerData.setMessagesSent(playerData.getMessagesSent() + 1);
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
