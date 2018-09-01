package com.fadelands.bungeecore.pm;

import com.fadelands.bungeecore.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SuppressWarnings("ALL")
public class ReplyCommand extends Command {

    public ReplyCommand(){
        super("reply", null, "r", "answer", "er", "ereply");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new ComponentBuilder("This can only be used ingame").create());
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length == 0) {
            player.sendMessage(new ComponentBuilder("§cYou need to enter a message to send.").color(ChatColor.RED).create());
            return;
        }

        if (PMManager.getLast(player) == null) {
            player.sendMessage(new ComponentBuilder("§cThe player you last messages has logged off.").color(ChatColor.RED).create());
            return;
        }

        ProxiedPlayer target = PMManager.getLast(player);

        if (target.getName().equals(player.getName())) {
            player.sendMessage(new ComponentBuilder("§cYou can't reply to yourself.").color(ChatColor.RED).create());
            return;
        }
        if (!(PMManager.isAllowingDms(player))) {
            player.sendMessage(new ComponentBuilder("§cYou turned your private messages off. Turn them back on with /settings.").color(ChatColor.RED).create());
            return;
        }

        if (!(PMManager.isAllowingDms(target))) {
            player.sendMessage(new ComponentBuilder("§cThe player you are trying to reply to is no longer allowing private messages.").color(ChatColor.RED).create());
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++)
            sb.append(args[i] + " ");
        String message = sb.toString();

        target.sendMessage(new ComponentBuilder("§8[§2" + player.getName() + " §6-> §2me§8] §7" + message).color(ChatColor.DARK_AQUA).create());
        player.sendMessage(new ComponentBuilder("§8[§2me §6-> §2" + target.getName() + "§8] §7" + message).color(ChatColor.DARK_AQUA).create());

        Connection connection = null;
        PreparedStatement ps = null;

        try{
            connection = Main.getConnection();
            ps =  connection.prepareStatement("INSERT INTO fadelands_chat_pms (sender_uuid,sender_name,receiver_uuid,receiver_name,date,message) " +
                    "VALUES (?,?,?,?,?,?)");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, player.getName());
            ps.setString(3, target.getUniqueId().toString());
            ps.setString(4, target.getName());
            ps.setTimestamp(5, new java.sql.Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
            ps.setString(6, message);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();

        } finally {
            Main.closeComponents(ps, connection);
        }
    }

}