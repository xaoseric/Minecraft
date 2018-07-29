package com.fadelands.core.commands.playerlist;

import com.fadelands.core.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ListCommandExecutor extends ListCommandHandler {

    public ListCommandExecutor(CorePlugin plugin) {
        super(plugin);
    }

    @Override
    public void handleListCommand(Player player) {
        player.sendMessage("§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=");
        player.sendMessage("§3Players Online [" + Bukkit.getServer().getOnlinePlayers().size() + "§b/§3" + Bukkit.getServer().getMaxPlayers() + "]:");
        player.sendMessage("§c[Administrators]: " + this.admins);
        player.sendMessage("§6[Senior Moderators]: " + this.senior);
        player.sendMessage("§6[Moderators]: " + this.mod);
        player.sendMessage("§e[Trainees]: " + this.trainee);
        player.sendMessage("§9[Builders]: " + this.builder);
        player.sendMessage("§b[Partner]: " + this.partner);
        player.sendMessage("§d[Media]: " + this.media);
        player.sendMessage("§5[Platinum]: " + this.platinum);
        player.sendMessage("§3[Premium]: " + this.premium);
        player.sendMessage("§a[Donator]: " + this.donator);
        player.sendMessage("§7[Users]: " + this.members);
        player.sendMessage("§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=§6=§c=");

        }
    }

