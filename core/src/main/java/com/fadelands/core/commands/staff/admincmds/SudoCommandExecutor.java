package com.fadelands.core.commands.staff.admincmds;

import com.fadelands.array.player.User;
import com.fadelands.core.CorePlugin;
import com.fadelands.array.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.jws.soap.SOAPBinding;

public class SudoCommandExecutor implements CommandExecutor {

    public CorePlugin plugin;
    public SudoCommandExecutor(CorePlugin plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("This can only be used ingame.");
            return true;
        }
        Player player = (Player) sender;
        if(!(new User().isAdmin(player.getName()))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§cInvalid usage. /sudo <player> </command or message>");
            return true;
        }

        String user = args[0];
        StringBuilder input = new StringBuilder();
        int i = 1;
        while (i < args.length) {
            input.append(args[i]).append(" ");
            i++;
        }
        input = new StringBuilder(input.toString().trim());
        boolean command = input.toString().startsWith("/");
        CommandSender u;
        if (user.equalsIgnoreCase("c")) {
            u = Bukkit.getConsoleSender();
        } else {
            u = Bukkit.getPlayer(user);
        }
        if (u == null) {
            player.sendMessage("§cThat is not a valid player.");
            return false;

        }
        if(args.length == 1) {
            sender.sendMessage("§cInput may not be null. /sudo <player> </command or message>");
            return false;
        }

        if (command) {
            input = new StringBuilder(input.substring(1));
            Player pu = (Player) u;
            Bukkit.dispatchCommand(u, input.toString());
            sender.sendMessage("§aSuccessfully made " + pu.getName() + " run command §2[§7" + input + "§2]§a.");
        } else {
            if (!(u instanceof Player)) {
                sender.sendMessage("§cThe target you specified can not use chat.");
                return false;
            }

            Player p = (Player) u;
            p.chat(input.toString());
            sender.sendMessage("§aSuccessfully made " + p.getName() + " say §2[§7" + input + "§2]§a.");
        }
        return false;
    }
}
