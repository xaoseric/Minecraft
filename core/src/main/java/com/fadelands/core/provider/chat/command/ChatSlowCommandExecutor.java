package com.fadelands.core.provider.chat.command;

import com.fadelands.array.utils.Utils;
import com.fadelands.core.CorePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatSlowCommandExecutor implements CommandExecutor {

    private CorePlugin corePlugin;

    public ChatSlowCommandExecutor(CorePlugin corePlugin) {
        this.corePlugin = corePlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("§cThis command can only be used ingame.");
            return true;
        }

        Player player = (Player) sender;
        if(!(player.hasPermission("fadelands.chatslow"))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length == 0) {
            player.sendMessage(Utils.Prefix + "§cInvalid usage. /chatslow <seconds> or \"/chatslow 0\" to disable.");
            return true;
        }

        int number = Integer.parseInt(args[0]);

        if(number == 0){
            corePlugin.getServerChat().setChatSlow(0, true);
            player.sendMessage(Utils.Prefix + "§cDisabled chat slow.");
            return true;
        }
        corePlugin.getServerChat().setChatSlow(number, true);
        player.sendMessage(Utils.Prefix + "§aEnabled chat slow with §2" + number + " " + (number < 1 ? "second" : "seconds" + " §adelay."));

        return false;

    }
}
