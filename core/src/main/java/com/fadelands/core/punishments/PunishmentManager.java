package com.fadelands.core.punishments;

import com.fadelands.core.Core;
import com.fadelands.core.player.UserUtil;
import com.fadelands.core.utils.Callback;
import com.fadelands.core.utils.UtilTime;
import com.fadelands.core.utils.Utils;
import com.fadelands.core.utils.keygenerators.AppealKeyGenerator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PunishmentManager implements Listener {

    private Core plugin;
    private PunishmentHandler punishmentHandler;

    public PunishmentManager(Core plugin) {
        this.plugin = plugin;
        this.punishmentHandler = new PunishmentHandler();
    }

    /*
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        loadPunishments(event.getPlayer().getUniqueId(), (data) -> {
            if (data != null) data.um();
        });
    }*/

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PunishPlayerJoinEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        loadPunishments(uuid, (data) -> {
            if(data == null) {
                return;
            }

            List<Punishment> punishments = data.getActivePunishments(PunishmentType.Ban);

            if (punishments.size() > 0) {

                for (Punishment active : punishments) {
                    if (!active.hasExpired() || active.isPermanent()) {
                        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, getBanMessage(active));
                        continue;
                    }

                    punishmentHandler.unpunish(active.getAppealKey());
                }
            }
        });
    }

    private String getBanMessage(Punishment punishment) {
        String banMessage = "§c§lYour account has been suspended from the FadeLands Network." + "\n\n§r" + punishment.getReason() + "\n\n";

        if (punishment.isPermanent()) banMessage += "§cThis punishment is permanent and will not expire.\n\n" + getAppealMessage(punishment);
        else banMessage += "§cYour punishment expires in " + UtilTime.MakeStr(punishment.getRemaining()) + ".\n\n" + getAppealMessage(punishment);

        return banMessage;
    }

    private String getAppealMessage(Punishment punishment) {
        return "§cYour Appeal Key: §f" + punishment.getAppealKey() + "\n" +
                "§7§o(Do not share this key with anyone, or it might affect the process of your appeal)\n\n" +
                "§6If you believe you were unfairly punished,\n" +
                "§6please dispute your punishment at www.fadelands.com/appeal.";
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void PunishChatEvent(AsyncPlayerChatEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        loadPunishments(uuid, (data) -> {
            if (data == null) {
                return;
            }

            List<Punishment> punishments = data.getActivePunishments(PunishmentType.Mute);

            if (punishments.size() > 0) {

                for (Punishment active : punishments) {
                    if (!active.hasExpired() || active.isPermanent()) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage("§r \n§cYou are currently muted for §l" + active.getReason() + "§c for §l"
                                + (active.isPermanent() ? "forever" : UtilTime.MakeStr(active.getRemaining())) + "§c.\n" + "§6§nYour Appeal Key: " + active.getAppealKey() + "§6.\n §r");
                        continue;
                    }

                    punishmentHandler.unpunish(active.getAppealKey());
                }
            }
        });
    }

    public void addPunishment(CommandSender sender, final String playerName, UUID playerUuid, final String reason, final UUID callerUuid, PunishmentType type, long duration, boolean permanent) {

        loadPunishments(playerUuid, (data) -> {

            if(data == null) {
                return;
            }

            if (type == PunishmentType.Ban && data.hasActive(PunishmentType.Ban)) {
                sender.sendMessage(Utils.Prefix_Red + "§cThis player is already banned.");
                return;
            }

            if (type == PunishmentType.Mute && data.hasActive(PunishmentType.Mute)) {
                sender.sendMessage(Utils.Prefix_Red + "§cThis player is already muted.");
                return;
            }

            final long now = System.currentTimeMillis();
            final String appealKey = new AppealKeyGenerator().nextKey();

            Punishment punishment = new Punishment(appealKey, callerUuid, type, reason, playerUuid, now, duration, true, permanent,false, null, null);
            data.addPunishment(punishment);
            punishmentHandler.punish(appealKey, callerUuid, type, reason, playerUuid, now, duration, permanent);

            Player target = Bukkit.getPlayer(playerUuid);

            switch (type) {
                case Ban:
                    if (target != null) {
                        Bukkit.getPlayer(playerUuid).kickPlayer(getBanMessage(punishment));
                        Bukkit.broadcastMessage(Utils.Alert + "§c§lA player on this server has been removed for breaking the rules. §6Thank you for reporting!");
                    }

                    Objects.requireNonNull(UserUtil.getOnlineStaff()).sendMessage(Utils.Alert + "§a" + playerName + " has been banned for \"" + reason + "\" for a period of " + (permanent ? "forever" : UtilTime.MakeStr(duration)) + " by " +  sender.getName() + ".");
                    break;
                case Mute:
                    if (target != null) {
                        String appeal = "§6§nYour Appeal Key: " + appealKey + "§r \n §r";
                        String muteMessage = "§r \n§c§l(!) You have been muted!\n§c" + "Reason: " + reason + ". \n";

                        if(permanent) muteMessage += "§cThis mute is permanent and will not expire.\n§r" + appeal + "\n§r";
                        else muteMessage += "§cYour punishment expires in §l" + UtilTime.MakeStr(duration) + "§c.\n§r" + appeal + "\n§r";

                        Bukkit.getPlayer(playerUuid).sendMessage(muteMessage);
                    }

                    Objects.requireNonNull(UserUtil.getOnlineStaff()).sendMessage(Utils.Alert + "§a" + playerName + " has been muted for \"" + reason + "\" for a period of " + (permanent ? "forever" : UtilTime.MakeStr(duration)) + " by " + sender.getName() + ".");
                    break;
                default:
                    break;
            }
        });

        }


    public void loadPunishments(UUID uuid, Callback<PunishmentData> callback) {

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
                        rs.getBoolean("permanent"),
                        rs.getBoolean("removed"),
                        rs.getString("remove_reason"),
                        rs.getString("remove_admin") == null ? null : UUID.fromString(rs.getString("remove_admin")));
                data.addPunishment(punishment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            plugin.getDatabaseManager().closeComponents(rs, ps, connection);
            callback.call(data);
        }
    }

    public long getTimeToPunish(String[] args, CommandSender sender) {
        if (args.length < 2) { // if no time is given, time is permanent
            return 0;
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
        return 0;
    }

}