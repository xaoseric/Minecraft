package com.fadelands.core.punishments.commands;

import com.fadelands.core.Core;
import com.fadelands.core.player.UserUtil;
import com.fadelands.core.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PunishCommand implements CommandExecutor {

    public final static Map<UUID, String> currentPunishReason = new HashMap<>();
    public final static Map<UUID, String> currentPunishTarget = new HashMap<>();
    private Core plugin;

    public PunishCommand(Core plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cThis can only be used ingame.");
            return true;
        }

        Player player = (Player) sender;
        if(!UserUtil.isStaff(player.getName())) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length < 2){
            player.sendMessage(Utils.Prefix_Red + "§cInvalid usage. /punish <player> <reason>");
            return true;
        }

        final String targetName = args[0];
        //noinspection deprecation

        if(!UserUtil.hasPlayedBefore(targetName)){
            sender.sendMessage(Utils.Prefix_Red + "§cThat's not a valid player.");
            return true;
        }

        String reason = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));

            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try{
                connection = plugin.getDatabaseManager().getConnection();
                ps = connection.prepareStatement("SELECT * FROM luckperms_players WHERE uuid=?");
                ps.setString(1, player.getUniqueId().toString());
                rs = ps.executeQuery();
                if(rs.next()){
                    switch (rs.getString("primary_group").toLowerCase()){
                        case "owner":
                            plugin.getPunishmentMenu().openAsAdmin(player, targetName, reason);
                            break;
                        case "admin":
                            plugin.getPunishmentMenu().openAsAdmin(player, targetName, reason);
                            break;
                        case "developer":
                            player.sendMessage("Developer is a non-mod rank and can therefore not use the punishment menu.");
                            break;
                        case "senior":
                            plugin.getPunishmentMenu().openAsSenior(player, targetName, reason);
                            break;
                        case "mod":
                            plugin.getPunishmentMenu().openAsMod(player, targetName, reason);
                            break;
                        case "trainee":
                            plugin.getPunishmentMenu().openAsTrainee(player, targetName, reason);
                            break;
                        default:
                            break;
                    }
                    currentPunishTarget.put(player.getUniqueId(), targetName);
                    currentPunishReason.put(player.getUniqueId(), reason);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                plugin.getDatabaseManager().closeComponents(rs,ps,connection);
            }


        return false;
    }
}
