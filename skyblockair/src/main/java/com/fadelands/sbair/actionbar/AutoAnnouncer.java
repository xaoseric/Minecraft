package com.fadelands.sbair.actionbar;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutoAnnouncer extends BukkitRunnable {


    public void run() {
        List<String> messages = new ArrayList<>();

        messages.add("§f§lTired of using commands? Try out §b§l/menu§f§l.");
        messages.add("§f§lYou can always shop anything you need with §b§l/shop§f§l.");
        messages.add("§f§lWe would love to hear your feedback on our new realms! Visit our forums for more info.");

        Random random = new Random();
        String announceMessage = messages.get(random.nextInt(messages.size()));
                ActionBar abar = new ActionBar(announceMessage);
                abar.sentToAll();
            }
        }
