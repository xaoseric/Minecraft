package com.fadelands.array.commands.moderator.punishment;

import com.fadelands.array.Array;
import com.fadelands.array.manager.GUIManager;
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

    public final static Map<UUID, Punishment> currentPunish = new HashMap<>();
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

        String targetraw = args[0];
        //noinspection deprecation
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetraw);

        if(target == null){
            sender.sendMessage(Utils.Prefix_Red + "§cThat's not a valid player.");
            return true;
        }

        String reason = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
        Punishment punishment = new Punishment(target, player.getUniqueId(), Punishment.PunishmentType.KICK);
        punishment.setReason(new CustomReason() {
            @Override
            public String getMessage() {
                return reason;
            }
        });
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
                            array.getPunishmentMenu().openAsAdmin(player, targetraw, reason);
                            break;
                        case "admin":
                            array.getPunishmentMenu().openAsAdmin(player, targetraw, reason);
                            break;
                        case "senior":
                            array.getPunishmentMenu().openAsSenior(player, targetraw, reason);
                            break;
                        case "mod":
                            array.getPunishmentMenu().openAsMod(player, targetraw, reason);
                            break;
                        case "trainee":
                            array.getPunishmentMenu().openAsTrainee(player, targetraw, reason);
                            break;
                        default:
                            break;
                    }
                    currentPunish.put(player.getUniqueId(), punishment);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                Array.closeComponents(rs,ps,connection);
            }


        return false;
    }
}
