package com.fadelands.core.commands.playerlist;

import com.fadelands.array.Array;
import com.fadelands.core.CorePlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class ListCommandHandler implements Listener {

    protected String admins, senior, mod, trainee, builder, partner, media, platinum, premium, donator, members;

    private List<Player> hiddenStaffMembers = new ArrayList<>();

    private List<Player> adminsOnline = new LinkedList<>();
    private List<Player> seniorsOnline = new LinkedList<>();
    private List<Player> modsOnline = new LinkedList<>();
    private List<Player> traineesOnline = new LinkedList<>();
    private List<Player> buildersOnline = new LinkedList<>();
    private List<Player> partnersOnline = new LinkedList<>();
    private List<Player> mediasOnline = new LinkedList<>();
    private List<Player> platinumsOnline = new LinkedList<>();
    private List<Player> premiumsOnline = new LinkedList<>();
    private List<Player> donatorsOnline = new LinkedList<>();
    private List<Player> membersOnline = new LinkedList<>();

    public CorePlugin plugin;

    public ListCommandHandler(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.addPlayerToList(event.getPlayer());
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        this.removePlayerFromList(event.getPlayer());
    }

    public abstract void handleListCommand(Player player);

    public void hideStaffFromList(Player player) {
        this.hiddenStaffMembers.add(player);
    }

    public void addPlayerToList(Player player) {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String query = "SELECT * FROM luckperms_players WHERE uuid='" + player.getUniqueId().toString() + "'";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("primary_group").equals("owner") || (rs.getString("primary_group").equals("admin") || (rs.getString("primary_group").equals("developer")))) {
                    this.adminsOnline.add(player);
                } else if (rs.getString("primary_group").equals("senior")) {
                    this.seniorsOnline.add(player);
                } else if (rs.getString("primary_group").equals("mod")) {
                    this.modsOnline.add(player);
                } else if (rs.getString("primary_group").equals("trainee")) {
                    this.traineesOnline.add(player);
                } else if (rs.getString("primary_group").equals("builder")) {
                    this.buildersOnline.add(player);
                } else if (rs.getString("primary_group").equals("partner")) {
                    this.partnersOnline.add(player);
                } else if (rs.getString("primary_group").equals("media")) {
                    this.mediasOnline.add(player);
                } else if (rs.getString("primary_group").equals("contributor")) {
                    this.platinumsOnline.add(player);
                } else if (rs.getString("primary_group").equals("platinum")) {
                    this.platinumsOnline.add(player);
                } else if (rs.getString("primary_group").equals("premium")) {
                    this.premiumsOnline.add(player);
                } else if (rs.getString("primary_group").equals("donator")) {
                    this.donatorsOnline.add(player);
                } else if (rs.getString("primary_group").equals("default")) {
                    this.membersOnline.add(player);
                }
            }
        } catch (SQLException e) {
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
    }

    public void removePlayerFromList(Player player) {
        if (this.adminsOnline.remove(player)) {
        } else if (this.seniorsOnline.remove(player)) {
        } else if (this.modsOnline.remove(player)) {
        } else if (this.traineesOnline.remove(player)) {
        } else if (this.buildersOnline.remove(player)) {
        } else if (this.partnersOnline.remove(player)) {
        } else if (this.mediasOnline.remove(player)) {
        } else if (this.platinumsOnline.remove(player)) {
        } else if (this.premiumsOnline.remove(player)) {
        } else if (this.donatorsOnline.remove(player)) {
        } else if (this.membersOnline.remove(player)) {
        }
    }

    public void updateListCommandStrings() {
        this.admins = this.serializePlayerNames(this.adminsOnline);
        this.senior = this.serializePlayerNames(this.seniorsOnline);
        this.mod = this.serializePlayerNames(this.modsOnline);
        this.trainee = this.serializePlayerNames(this.traineesOnline);
        this.builder = this.serializePlayerNames(this.buildersOnline);
        this.partner = this.serializePlayerNames(this.partnersOnline);
        this.media = this.serializePlayerNames(this.mediasOnline);
        this.platinum = this.serializePlayerNames(this.platinumsOnline);
        this.premium = this.serializePlayerNames(this.premiumsOnline);
        this.donator = this.serializePlayerNames(this.donatorsOnline);
        this.members = this.serializePlayerNames(this.membersOnline);

    }

    private String serializePlayerNames(List<Player> list) {
        StringBuilder sb = new StringBuilder();

        if (list.size() > 0) {
            for (Player player : list) {
                sb.append(", ");
                sb.append(player.getDisplayName());
                sb.append(ChatColor.RESET);
            }

            return sb.substring(2);
        } else {
            return "None";
        }
    }

    public List<Player> getHiddenStaffMembers() {
        return this.hiddenStaffMembers;
    }
    public List<Player> getAdminsOnline() {
        return this.adminsOnline;
    }
    public List<Player> getSeniorsOnline() {
        return this.seniorsOnline;
    }
    public List<Player> getModsOnline() {
        return this.modsOnline;
    }
    public List<Player> getTraineesOnline() {
        return this.traineesOnline;
    }
    public List<Player> getBuildersOnline() {
        return this.buildersOnline;
    }
    public List<Player> getPartnersOnline() {
        return this.partnersOnline;
    }
    public List<Player> getMediasOnline() {
        return this.mediasOnline;
    }
    public List<Player> getPlatinumsOnline() {
        return this.platinumsOnline;
    }
    public List<Player> getPremiumsOnline() {
        return this.premiumsOnline;
    }
    public List<Player> getDonatorsOnline() {
        return this.donatorsOnline;
    }
    public List<Player> getMembersOnline() {
        return this.membersOnline;
    }

}