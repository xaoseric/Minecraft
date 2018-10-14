package com.fadelands.core.punishments;

import com.fadelands.core.Core;
import com.fadelands.core.utils.TimeUtils;
import com.fadelands.core.utils.UtilTime;
import com.fadelands.core.utils.Utils;
import com.fadelands.core.utils.keygenerators.AppealKeyGenerator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class PunishmentManager implements Listener {

    private Core plugin;
    private PunishmentHandler punishmentHandler;

    public PunishmentManager(Core plugin) {
        this.plugin = plugin;
        this.punishmentHandler = new PunishmentHandler();
    }

    public HashMap<UUID, PunishmentData> punishData = new HashMap<>();

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        punishData.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PunishPlayerJoinEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        loadPunishments(event.getUniqueId());

        PunishmentData data = punishData.get(uuid);

        if (data.isBanned()) {
            Punishment punishment = data.getPunishment(PunishmentType.Ban);

            if (punishment == null) {
                return;
            }

            if(punishment.getRemaining() < 0) { //expired?
                punishmentHandler.unpunish(punishment.getAppealKey());
                return;
            }

            String appeal = "§cYour Appeal Key: §f" + punishment.getAppealKey() + "\n" +
                    "§7§o(Do not share this key with anyone, or it might affect the process of your appeal)\n\n" +
                    "§6If you believe you were unfairly punished,\n" +
                    "§6please dispute your punishment at www.fadelands.com/appeal.";

            String banMessage = "§c§lYour account has been suspended from the FadeLands Network." + "\n" +
                    "\n" + "§r" + punishment.getReason() + "\n\n";

            if (punishment.getExpirationTime() == 1) {
                banMessage += "§cThis punishment is permanent and will not expire.\n\n" + appeal;
            } else {
                banMessage += "§cYour punishment expires in " + UtilTime.MakeStr(punishment.getRemaining()) + ".\n\n" + appeal;
            }
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, banMessage);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PunishChatEvent(AsyncPlayerChatEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        PunishmentData data = punishData.get(uuid);

        if (data.isMuted()) {
            Punishment punishment = data.getPunishment(PunishmentType.Mute);

            if (punishment == null) {
                return;
            }

            if(punishment.getRemaining() < 0) { //expired?
                punishmentHandler.unpunish(punishment.getAppealKey());
                return;
            }

            event.getPlayer().sendMessage("§r\n§cYou are currently muted for §l" + punishment.getReason() + "§c for §l" + (punishment.getRemaining() == 1 ? "forever" : UtilTime.MakeStr(punishment.getRemaining())) + "§c.\n" +
                    "§6§nYour Appeal Key: " + punishment.getAppealKey() + "§6.\n§r");
            event.setCancelled(true);
        }
    }

    public void addPunishment(CommandSender sender, final String playerName, UUID playerUuid, final String reason, final UUID callerUuid, boolean ban, long duration) {
        if (!punishData.containsKey(playerUuid)) {
            punishData.put(playerUuid, new PunishmentData(playerUuid));
        }

        loadPunishments(playerUuid); //if player is offline, we need to load their punishments again to check if they're already punished

        PunishmentData data = punishData.get(playerUuid);

        Punishment banPunishment = data.getPunishment(PunishmentType.Ban);
        System.out.println(banPunishment);
        if (banPunishment != null && banPunishment.getPunishmentType() == PunishmentType.Ban && banPunishment.isBanned()) {
            sender.sendMessage(Utils.Prefix_Red + "§cThis player is already banned.");
            return;
        }

        Punishment mutePunishment = data.getPunishment(PunishmentType.Mute);
        System.out.println(mutePunishment);
        if (mutePunishment != null && mutePunishment.getPunishmentType() == PunishmentType.Mute && mutePunishment.isMuted()) {
            sender.sendMessage(Utils.Prefix_Red + "§cThis player is already muted.");
            return;
        }

        final long now = System.currentTimeMillis();
        final PunishmentType type = !ban ? PunishmentType.Mute : PunishmentType.Ban;

        final String appealKey = new AppealKeyGenerator().nextKey();

        if (duration == 1) { // perm
            Punishment punishment = new Punishment(appealKey, callerUuid, type, reason, playerUuid, now, 1, true, false, null, null);
            data.addPunishment(punishment);
            punishmentHandler.punish(appealKey, callerUuid, type, reason, playerUuid, now, 1);
        } else {
            Punishment punishment = new Punishment(appealKey, callerUuid, type, reason, playerUuid, now, duration, true, false, null, null);
            data.addPunishment(punishment);
            punishmentHandler.punish(appealKey, callerUuid, type, reason, playerUuid, now, duration);
        }

        if (ban) {
            String appeal = "§cYour Appeal Key: §f" + appealKey + "\n" +
                    "§7(Do not share this key with anyone, or it might affect the process of your appeal)\n\n" +
                    "§6If you believe you were unfairly punished,\n" +
                    "§6please dispute your punishment at www.fadelands.com/appeal.";

            String kickMessage = "§c§lYour account has been suspended from the FadeLands Network." + "\n" +
                    "\n" + "§r" + reason + "\n\n";

            if(duration == 1) { // perm
                kickMessage += "§cThis punishment is permanent and will not expire.\n\n" + appeal;
            } else {
                kickMessage += "§cYour punishment expires in " + UtilTime.MakeStr(duration) + ".\n\n" + appeal;
            }

            sender.sendMessage(Utils.Prefix + "§a" + playerName + " has been banned for \"" + reason + "\" for a period of " + (duration == 1 ? "forever" : UtilTime.MakeStr(duration)) + ".");

            if (Bukkit.getPlayer(playerUuid).isOnline()) {
                Bukkit.getPlayer(playerUuid).kickPlayer(kickMessage);

                Bukkit.broadcastMessage(Utils.Alert + "§c§lA player in this server has been removed for breaking the rules. §6Thank you for reporting!");
            }
            // add load player here if not working
        }

        if (!ban) {
            String appeal = "§6§nYour Appeal Key: " + appealKey + "\n §r";

            String muteMessage = "§r \n" +
                    "§c§l(!) You have been muted!" + "\n" +
                    "§c" + "You have been muted for " + reason + ". \n";
            if(duration == 1) { // perm
                muteMessage += "§cThis mute is permanent and will not expire.\n§r" + appeal + "\n§r";
            } else {
                muteMessage += "§cYour punishment expires in §l" + UtilTime.MakeStr(duration) + "§c.\n§r" + appeal + "\n§r";
            }

            sender.sendMessage(Utils.Prefix_Green + "§a" + playerName + " has been muted successfully for \"" + reason + "\" for a period of " + (duration == 1 ? "forever" : UtilTime.MakeStr(duration)) + ".");

            if (Bukkit.getPlayer(playerUuid).isOnline()) {
                Bukkit.getPlayer(playerUuid).sendMessage(muteMessage);
            }
        }
    }

    public void loadPunishments(UUID uuid) {

        PunishmentData data = new PunishmentData(uuid);

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM punishments WHERE punished_uuid = ?";

        try {
            connection = plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, uuid.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                Punishment punishment = new Punishment(rs.getString("appeal_key"),
                        UUID.fromString(rs.getString("punisher_uuid")),
                        PunishmentType.getById(rs.getInt("punish_type")),
                        rs.getString("reason"),
                        UUID.fromString(rs.getString("punished_uuid")),
                        rs.getLong("date"),
                        rs.getLong("until"),
                        rs.getBoolean("active"),
                        rs.getBoolean("removed"),
                        rs.getString("remove_reason"),
                        rs.getString("remove_admin"));
                data.addPunishment(punishment);
            }
            punishData.put(uuid, data);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            plugin.getDatabaseManager().closeComponents(rs, ps, connection);
        }
    }

    public long getTimeToPunish(String[] args, CommandSender sender) {
        if (args.length < 2) { // if no time is given, time is permanent
            return -1;
        }
        if (args[1].substring(0, 1).matches("[0-9]")) {
            try {
                char timeLength = args[1].charAt(args[1].length() - 1);
                long time = Long.parseLong(args[1].substring(0, args[1].length() - 1));

                if (timeLength == 's' || timeLength == 'S') {
                    time = time * 1000;
                } else if (timeLength == 'm' || timeLength == 'M') {
                    time = time * 1000 * 60;
                } else if (timeLength == 'h' || timeLength == 'H') {
                    time = time * 1000 * 60 * 60;
                } else if (timeLength == 'd' || timeLength == 'D') {
                    time = time * 1000 * 60 * 60 * 24;
                } else if (timeLength == 'w' || timeLength == 'W') {
                    time = time * 1000 * 60 * 60 * 24 * 7;
                } else if (timeLength == 'n' || timeLength == 'N') {
                    time = time * 1000 * 60 * 60 * 24 * 30;
                } else if (timeLength == 'y' || timeLength == 'Y') {
                    time = time * 1000 * 60 * 60 * 24 * 365;
                } else {
                    sender.sendMessage(Utils.Prefix_Red + "§cNot a valid date format. (#s/m/h/d/w/n)");
                    return -30;
                }
                return time;
            } catch (Exception e) {
                sender.sendMessage(Utils.Prefix_Red + "§c" + args[1] + " is an invalid date format.");
                return -20;
            }
        }
        return -1;
    }

}
