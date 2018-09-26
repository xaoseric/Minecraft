package com.fadelands.dailyrewards.rewardman.command;

import com.fadelands.dailyrewards.Main;
import com.fadelands.dailyrewards.rewardman.Rewards;
import com.fadelands.dailyrewards.rewardman.inventory.RewardInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RewardsCommand implements CommandExecutor {

    private Main plugin;

    public RewardsCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cThis can only be used ingame.");
            return true;
        }
        Player player = (Player) sender;
        new RewardInventory(plugin).openInventory(player);
        return false;
    }
}
