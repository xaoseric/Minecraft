package com.fadelands.dailyrewards.rewardman;

import com.fadelands.array.utils.Utils;
import com.fadelands.dailyrewards.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class RewardMan {

    private HashMap<Entity,Location> entityMove = new HashMap<>();

    private Main plugin;

    public RewardMan(Main plugin) {
        this.plugin = plugin;
    }

    public void spawnRewardMan(Location location, Player spawner) {
        boolean rewardManExists = plugin.getRewardManFile().getBoolean("exists");
        if (rewardManExists) {
            spawner.sendMessage(Utils.Prefix + "§cA Reward Man already exists at " + plugin.getRewardManFile().getDouble("x") + ", " + plugin.getRewardManFile().getDouble("y") + ", " + plugin.getRewardManFile().getDouble("z") + ".");
            return;
        }

        Villager villager = (Villager) Bukkit.getServer().getWorld("world").spawnEntity(location, EntityType.VILLAGER);
        villager.setCustomName("§6§lReward Man");
        villager.setCustomNameVisible(true);
        villager.setAdult();
        villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 100));
        villager.setProfession(Villager.Profession.LIBRARIAN);
        entityMove.put(villager, location);

        plugin.getRewardManFile().set("exists", true);
        plugin.getRewardManFile().set("x", location.getX());
        plugin.getRewardManFile().set("y", location.getY());
        plugin.getRewardManFile().set("z", location.getZ());
        plugin.getRewardManFile().set("yaw", location.getYaw());
        plugin.getRewardManFile().set("pitch", location.getPitch());
        File file = new File(plugin.getDataFolder() + File.separator + "rewardman.yml");
        try {
            plugin.getRewardManFile().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        spawner.sendMessage(Utils.Prefix + "§2Spawned a reward man at your location.");

        new BukkitRunnable() {

            @Override
            public void run() {
                villager.teleport(entityMove.get(villager));
            }
        }.runTaskTimerAsynchronously(plugin, 0, 0);
    }

    public void removeRewardMan(Player despawner) {
        boolean rewardManExists = plugin.getRewardManFile().getBoolean("exists");
        if (!(rewardManExists)) {
            despawner.sendMessage(Utils.Prefix + "§cThere's no reward man in this server.");
            return;
        }

        for(Entity entity : Bukkit.getWorld("world").getEntities()) {
            if(entity.getType() == EntityType.VILLAGER && (entity.isCustomNameVisible() && (entity.getCustomName().equals("§6§lReward Man")))) {
                entity.remove();
                System.out.println(entity);

                Bukkit.getLogger().info(despawner.getName() + " removed reward man from the server.");
                despawner.sendMessage(Utils.Prefix + "§cRemoved the reward man from this server.");

                plugin.getRewardManFile().set("exists", false);

                File file = new File(plugin.getDataFolder() + File.separator + "rewardman.yml");
                try {
                    plugin.getRewardManFile().save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
