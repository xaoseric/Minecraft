package com.fadelands.bungeecore.pm;

import com.fadelands.bungeecore.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SuppressWarnings("ALL")
public class PrivateMessageCommand extends Command {

    public PrivateMessageCommand(){
        super("msg", null, "pm", "tell", "whisper", "emsg", "etell", "ewhisper", "m", "message", "w", "t");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)){
            sender.sendMessage(new ComponentBuilder("This can only be used ingame.").create());
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(args.length == 0){
            player.sendMessage(new ComponentBuilder("§cWho are you trying to message? A ghost?").color(ChatColor.RED).create());
            return;
        }

        String targetString = args[0];
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetString);
        if(target == null){
            sender.sendMessage(new ComponentBuilder("§cThat player is not online.").color(ChatColor.RED).create());
            return;
        }

        if(!(PMManager.isAllowingDms(player))){
            sender.sendMessage(new ComponentBuilder("§cYour messages are currently toggled off. Toggle them back on with /settings.").color(ChatColor.RED).create());
            return;
        }

        if(!(PMManager.isAllowingDms(target))){
            sender.sendMessage(new ComponentBuilder("§cThe player you are trying to message turned their private messages off.").color(ChatColor.RED).create());
            return;
        }

        if(args.length == 1){
            player.sendMessage(new ComponentBuilder("§cYou need to enter a message.").color(ChatColor.RED).create());
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++)
            sb.append(args[i] + " ");
        String message = sb.toString();

        target.sendMessage(new ComponentBuilder("§8[§2" + player.getName() + " §6-> §2me§8] §7" + message).color(ChatColor.GRAY).create());
        player.sendMessage(new ComponentBuilder("§8[§2me §6-> §2" + target.getName() + "§8] §7" + message).color(ChatColor.GRAY).create());

        PMManager.setLast(player, target);
        PMManager.setLast(target, player);

        Connection connection = null;
        PreparedStatement ps = null;

        try{
            connection = Main.getConnection();
            ps =  connection.prepareStatement("INSERT INTO chat_pms (sender_uuid,sender_name,receiver_uuid,receiver_name,date,message) " +
            "VALUES (?,?,?,?,?,?)");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, player.getName());
            ps.setString(3, target.getUniqueId().toString());
            ps.setString(4, target.getName());
            ps.setTimestamp(5,  new java.sql.Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
            ps.setString(6, message);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            Main.closeComponents(ps, connection);
        }
    }
}

