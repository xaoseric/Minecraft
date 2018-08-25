package com.fadelands.array.commands.moderator.punishments.commands;

import com.fadelands.array.Array;
import com.fadelands.array.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

public class PunishCommandExecutor implements CommandExecutor {

    public final static Map<UUID, String> currentPunishReason = new HashMap<>();
    public final static Map<UUID, String> currentPunishTarget = new HashMap<>();
    private Array array;

    public PunishCommandExecutor(Array array){
        this.array = array;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cThis can only be used ingame.");
            return true;
        }

        if(!(sender.hasPermission("fadelands.punish"))){
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        Player player = (Player) sender;
        if(args.length < 2){
            player.sendMessage(Utils.Prefix_Red + "§cInvalid usage. /punish <player> <reason>");
            return true;
        }

        final String targetName = args[0];
        //noinspection deprecation
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);

        if(target == null){
            sender.sendMessage(Utils.Prefix_Red + "§cThat's not a valid player.");
            return true;
        }

        String reason = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));

            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try{
                connection = Array.getConnection();
                ps = connection.prepareStatement("SELECT * FROM luckperms_players WHERE uuid=?");
                ps.setString(1, player.getUniqueId().toString());
                rs = ps.executeQuery();
                if(rs.next()){
                    switch (rs.getString("primary_group").toLowerCase()){
                        case "owner":
                            array.getPunishmentMenu().openAsAdmin(player, targetName, reason);
                            break;
                        case "admin":
                            array.getPunishmentMenu().openAsAdmin(player, targetName, reason);
                            break;
                        case "developer":
                            player.sendMessage("Developer is a non-moderator rank. You can still apply for moderator as a developer.");
                            break;
                        case "senior":
                            array.getPunishmentMenu().openAsSenior(player, targetName, reason);
                            break;
                        case "mod":
                            array.getPunishmentMenu().openAsMod(player, targetName, reason);
                            break;
                        case "trainee":
                            array.getPunishmentMenu().openAsTrainee(player, targetName, reason);
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
                Array.closeComponents(rs,ps,connection);
            }


        return false;
    }
}
