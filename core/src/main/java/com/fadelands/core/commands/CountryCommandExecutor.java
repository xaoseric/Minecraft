package com.fadelands.core.commands;

import com.fadelands.array.utils.Utils;
import com.fadelands.core.CorePlugin;
import com.fadelands.core.utils.CountryLookup;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CountryCommandExecutor implements CommandExecutor {

    public CorePlugin plugin;

    public CountryCommandExecutor(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This can only be used ingame!");
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("country")) {
            if (!(player.hasPermission("fadelands.country"))) {
                player.sendMessage(Utils.No_Perm);
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Utils.Prefix + "§cThat's not a valid player.");
                return true;
            }
                try {
                    player.sendMessage("Country of " + target.getName() + ": " + CountryLookup.getCountryCode(target.getAddress()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        return false;
    }
}