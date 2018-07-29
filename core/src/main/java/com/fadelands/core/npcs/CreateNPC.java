package com.fadelands.core.npcs;

import com.fadelands.array.utils.Utils;
import com.fadelands.core.CorePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateNPC implements CommandExecutor {
    public CorePlugin plugin;

    public CreateNPC(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This can only be used ingame!");
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("createnpc")) {
            if (!(player.hasPermission("fadelands.createnpc"))) {
                player.sendMessage(Utils.No_Perm);
                return true;
            }
            if (args.length == 0) {
                player.sendMessage(Utils.Prefix + "§cYou are missing arguments. /createnpc <NAME>");
                return true;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < args.length; i++)
                sb.append(args[i] + " ");
            String name = sb.toString();
            NPCs.CreateNpc(player, name);
            player.sendMessage(Utils.Prefix_Green + "§aCreated NPC " + args[0] + "!");
        }
        return false;
    }
}

