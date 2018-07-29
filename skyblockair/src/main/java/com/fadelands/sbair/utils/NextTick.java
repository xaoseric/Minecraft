package com.fadelands.sbair.utils;

import com.fadelands.sbair.Main;
import org.bukkit.Bukkit;

public class NextTick {

    public static Main plugin;
    public NextTick(Main plugin) {

        NextTick.plugin = plugin;
    }

    public static int scheduleTask(Runnable run, long delay) {
        return Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, run, delay);
    }

    public static int runTaskNextTick(Runnable run) {
        run.run();
        return scheduleTask(run, 1);
    }
    public static int runTaskTwoTicks(Runnable run) {
        run.run();
        return scheduleTask(run, 2);
    }

    public static int runTaskThreeTicks(Runnable run) {
        run.run();
        return scheduleTask(run, 3);
    }




}
