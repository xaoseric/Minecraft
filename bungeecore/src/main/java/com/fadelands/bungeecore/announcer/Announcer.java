package com.fadelands.bungeecore.announcer;

import com.fadelands.bungeecore.Main;
import com.fadelands.bungeecore.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Announcer {

    private List<String> announcements = new ArrayList<>();;

    private Main main;

    public Announcer(Main main){
        this.main = main;
    }

    public void startAnnouncements() {
        announcements.add(Utils.Announcement + "§fWe are searching for new staff members to join our team! Apply at §2https://fadelands.com/moderator/§f.");
        announcements.add(Utils.Announcement + "§fWe currently have §2§l40% OFF §fon our products. Check them out at §2https://store.fadelands.com/§f.");
        announcements.add(Utils.Announcement + "§fJoin our public Discord! We host giveaways, post announcements and a lot more, §2discord.fadelands.com§f.");
        announcements.add(Utils.Announcement + "§fSign up on our forums to be an even bigger part of our community, §2https://fadelands.com/§f.");

        ProxyServer.getInstance().getScheduler().schedule(main, new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                String announcement = announcements.get(random.nextInt(announcements.size()));
                ProxyServer.getInstance().broadcast(new ComponentBuilder(announcement).color(ChatColor.WHITE).create());
            }
        }, 1, 3, TimeUnit.MINUTES);
    }
}
