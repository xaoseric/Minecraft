package com.fadelands.core.provider.chat.announcements;

import com.fadelands.core.Core;
import com.fadelands.core.utils.PluginMessage;
import com.fadelands.core.utils.Utils;
import com.fadelands.core.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Announcements {

    private List<String> announcements;

    private Core plugin;
    private Settings settings;
    private PluginMessage pluginMessage;

    public Announcements(Core plugin, Settings settings, PluginMessage pluginMessage) {
        this.plugin = plugin;
        this.settings = settings;
        this.pluginMessage = pluginMessage;
        this.announcements = new ArrayList<>();
    }

    public void startAnnouncements(Player player) {
        announcements.add(Utils.Announcement + "§7We are searching for new staff members to join our team! Apply today @ §6https://fadelands.com/mod/§7.");

        announcements.add(Utils.Announcement + "§7We currently have §6§l40% OFF §7on our products. Check them out @ §2https://store.fadelands.com/§7.");

        announcements.add(Utils.Announcement + "§7Join our public Discord! We host giveaways, post announcements and a lot more, §6discord.fadelands.com§7.");

        announcements.add(Utils.Announcement + "§7Sign up on our forums to be an even bigger part of our community! §2https://fadelands.com/§7.");

        if(settings.showAnnouncements(player)) {

        new BukkitRunnable() {

            @Override
            public void run() {
                Random random = new Random();
                String announcement = announcements.get(random.nextInt(announcements.size()));
                player.sendMessage(announcement);

            }
        }.runTaskTimerAsynchronously(plugin, 80, 3600); //3600

        }

    }
}
