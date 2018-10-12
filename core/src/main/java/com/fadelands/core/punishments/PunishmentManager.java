package com.fadelands.core.punishments;

import com.fadelands.core.Core;
import com.fadelands.core.punishments.tokens.PunishClientToken;
import com.fadelands.core.punishments.tokens.PunishmentToken;
import com.fadelands.core.utils.TimeUtils;
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

import java.util.HashMap;

public class PunishmentManager implements Listener {

    private Core core;
    private PunishmentHandler punishmentHandler;
    public HashMap<String, PunishmentClient> punishClients;

    public PunishmentManager(Core core) {
        this.core = core;
        this.punishmentHandler = new PunishmentHandler();

        punishClients = new HashMap<>();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        punishClients.remove(event.getPlayer().getUniqueId().toString().toLowerCase());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PunishPlayerJoinEvent(AsyncPlayerPreLoginEvent event){
        if (!punishClients.containsKey(event.getUniqueId().toString().toLowerCase())){
            punishClients.put(event.getUniqueId().toString().toLowerCase(), new PunishmentClient());
        }

        String uuid = event.getUniqueId().toString().toLowerCase();

        PunishClientToken token = punishmentHandler.loadPunishClient(uuid);
        if(token == null) return;

        loadClient(uuid);
        PunishmentClient client = punishClients.get(uuid.toLowerCase());

        if (client.isBanned(uuid)) {
            Punishment punishment = client.getPunishment(uuid, PunishmentType.Ban);

                String appeal = "§cYour Appeal Key: §f" + punishment.getAppealKey() + "\n" +
                    "§7§o(Do not share this key with anyone, or it might affect the process of your appeal)\n\n" +
                    "§6If you believe you were unfairly punished,\n" +
                    "§6please dispute your punishment at www.fadelands.com/appeal.";

                String banMessage = "§c§lYour account has been suspended from the FadeLands Network." + "\n" +
                    "\n" + "§r" + punishment.getReason() + "\n\n";

            if(punishment.getUntil() == 0) {
                banMessage += "§cThis punishment is permanent and will not expire.\n\n" + appeal;
            }else{
                banMessage += "§cYour punishment expires in " + TimeUtils.toRelative(punishment.getRemaining()) + ".\n\n" + appeal;
            }
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, banMessage);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PunishChatEvent(AsyncPlayerChatEvent event) {
        if (!punishClients.containsKey(event.getPlayer().getUniqueId().toString().toLowerCase())){
            punishClients.put(event.getPlayer().getUniqueId().toString().toLowerCase(), new PunishmentClient());
        }

        String uuid = event.getPlayer().getUniqueId().toString().toLowerCase();

        PunishClientToken token = punishmentHandler.loadPunishClient(uuid);
        if(token == null) return;

        loadClient(uuid);
        PunishmentClient client = punishClients.get(uuid.toLowerCase());

        if (client.isMuted(uuid)) {
            Punishment punishment = client.getPunishment(uuid, PunishmentType.Mute);
            event.getPlayer().sendMessage("§r\n§cYou are currently muted for §l" + punishment.getReason() + "§c for §l" + TimeUtils.toRelative(punishment.getUntil()) + "§c.\n" +
                    "§6§nYour Appeal Key: " + punishment.getAppealKey() + "§6.\n§r");
            event.setCancelled(true);
        }
    }

    public void addPunishment(CommandSender sender, final String playerName, String playerUuid, final String reason, final String callerUuid, boolean ban, long duration) {
        if (!punishClients.containsKey(playerUuid.toLowerCase())) {
            punishClients.put(playerUuid.toLowerCase(), new PunishmentClient());
        }

        PunishClientToken token = punishmentHandler.loadPunishClient(playerUuid);

        if (token != null) {
            loadClient(playerUuid);
            PunishmentClient client = punishClients.get(playerUuid);
            Punishment banPunishment = client.getPunishment(playerUuid, PunishmentType.Ban);
            if (ban && banPunishment != null && banPunishment.isBanned()) {
                sender.sendMessage(Utils.Prefix_Red + "§cThis player is already banned.");
                return;
            }

            Punishment mutePunishment = client.getPunishment(playerUuid, PunishmentType.Mute);
            if (!ban && mutePunishment != null && mutePunishment.isMuted()) {
                sender.sendMessage(Utils.Prefix_Red + "§cThis player is already muted.");
                return;
            }
        }

        long date = System.currentTimeMillis();
        final long durationTime = date + duration;
        final PunishmentType type = !ban ? PunishmentType.Mute : PunishmentType.Ban;

        String appealKey = new AppealKeyGenerator().nextKey();

        if(duration == 0) {
            Punishment punishment = new Punishment(appealKey, callerUuid, type.ordinal(), reason, playerUuid, date, 0, true, false, null, null);
            getClient(playerUuid).addPunishment(playerUuid, punishment);
            punishmentHandler.punish(appealKey, callerUuid, type, reason, playerUuid, date, 0);
        }else{
            Punishment punishment = new Punishment(appealKey, callerUuid, type.ordinal(), reason, playerUuid, date, durationTime, true, false, null, null);
            getClient(playerUuid).addPunishment(playerUuid, punishment);
            punishmentHandler.punish(appealKey, callerUuid, type, reason, playerUuid, date, durationTime);
        }

        if (type == PunishmentType.Ban) {
            String appeal = "§cYour Appeal Key: §f" + appealKey + "\n" +
                    "§7(Do not share this key with anyone, or it might affect the process of your appeal)\n\n" +
                    "§6If you believe you were unfairly punished,\n" +
                    "§6please dispute your punishment at www.fadelands.com/appeal.";

            String kickMessage = "§c§lYour account has been suspended from the FadeLands Network." + "\n" +
                    "\n" + "§r" + reason + "\n\n";

            if(duration == 0) {
                kickMessage += "§cThis punishment is permanent and will not expire.\n\n" + appeal;
            } else {
                kickMessage += "§cYour punishment expires in " + TimeUtils.toRelative(duration) + ".\n\n" + appeal;
            }

            sender.sendMessage(Utils.Prefix_Green + "§a" + playerName + " has been banned successfully for \"" + reason + "\". for a period of " + TimeUtils.toRelative(duration) + ".");

            if (core.getPluginMessage().isOnline(playerName)) {
                core.getPluginMessage().kickPlayer(playerName, kickMessage);

                Bukkit.broadcastMessage("§7§l" + Utils.ArrowRight + " §c§lA player in this server has been removed for breaking the rules. §eThank you for reporting.");
            }
            // add load player here if not working
        }

        if (type == PunishmentType.Mute) {
            String appeal = "§6§nYour Appeal Key: " + appealKey;

            String muteMessage = "§r\n§c§l(!) You have been muted!" + "\n" +
                    "§c" + "You have been muted for " + reason + ". \n";
            if(duration == 0) {
                muteMessage += "§cThis mute is permanent and will not expire.\n§r" + appeal + "\n§r";
            } else {
                muteMessage += "§cYour punishment expires in §l" + TimeUtils.toRelative(duration) + "§c.\n§r" + appeal + "\n§r";
            }

            sender.sendMessage(Utils.Prefix_Green + "§a" + playerName + " has been muted successfully for \"" + reason + "\".");

            if (core.getPluginMessage().isOnline(playerName)) {
                core.getPluginMessage().sendMessage(playerName, muteMessage);
            }else{
                sender.sendMessage(Utils.Prefix + "§cCouldn't alert user due to not being online.");
            }
        }
    }

    public void loadClient(String uuid) {
        PunishmentClient client = new PunishmentClient();

        PunishClientToken token = punishmentHandler.loadPunishClient(uuid);
        PunishmentToken pToken = new PunishmentToken();

        pToken.appealKey = token.appealKey;
        pToken.punisherUuid = token.punisherUuid;
        pToken.punishType = token.punishType;
        pToken.reason = token.reason;
        pToken.punishedUuid = token.punishedUuid;
        pToken.date = token.date;
        pToken.until = token.until;
        pToken.active = token.active;
        pToken.removed = token.removed;
        pToken.removeReason = token.removeReason;
        pToken.removeAdmin = token.removeAdmin;
        client.addPunishment(uuid.toLowerCase(), new Punishment(pToken.appealKey, pToken.punishedUuid, pToken.punishType, pToken.reason,
                pToken.punishedUuid, pToken.date, pToken.until, pToken.active, pToken.removed, pToken.removeReason, pToken.removeAdmin));

        punishClients.put(uuid.toLowerCase(), client);
    }

    private PunishmentClient getClient(String uuid){
        synchronized (this) {
            return punishClients.get(uuid.toLowerCase());
        }
    }

    public long getTimeToPunish(String[] args, CommandSender sender) {
        if (args.length < 2) { // if no time is given (permanent)
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
                    return -1000000000;
                }
                return time;
            } catch (Exception e) {
                sender.sendMessage(Utils.Prefix_Red + "§c" + args[1] + " is an invalid date format.");
                return -30;
            }
        }
        return 0;
    }
}
