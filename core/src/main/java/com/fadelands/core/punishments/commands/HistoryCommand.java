package com.fadelands.core.punishments.commands;

import com.fadelands.core.Core;
import com.fadelands.core.punishments.*;
import com.fadelands.core.punishments.tokens.PunishClientToken;
import com.fadelands.core.player.User;
import com.fadelands.core.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class HistoryCommand implements CommandExecutor {

    private Core plugin;
    private HashMap<String, PunishmentClient> punishClients;
    private PunishmentHandler punishmentHandler;


    public HistoryCommand(Core plugin, PunishmentManager punishmentManager){
        this.plugin = plugin;
        punishClients = new HashMap<>();
        this.punishmentHandler = new PunishmentHandler();

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cThis can only be used ingame.");
            return true;
        }

        Player player = (Player) sender;
        if(!User.isMod(player.getName()) || !User.isAdmin(player.getName())) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

       if(args.length == 0){
           player.sendMessage(Utils.Prefix_Red + "§cYou are missing a few parameters. /history <target>");
           return true;
       }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String targetraw = args[0];

        try {
            connection = plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement("SELECT * FROM punishments WHERE punished_uuid = ?");
            ps.setString(1, User.getUuid(targetraw));
            rs = ps.executeQuery();
            if(!(rs.next())){
                player.sendMessage(Utils.Prefix_Red + "§cCouldn't find any punishment history of that user.");
            }else {
                player.sendMessage(Utils.Prefix_Green + "§aFound punishment history of " + targetraw + ".");


                Inventory inv = Bukkit.createInventory(null, 9 * 5, targetraw + "'s Punishments");
                String uuid = User.getUuid(targetraw);

                PunishmentManager punishmentManager = new PunishmentManager(plugin);
                PunishClientToken token = punishmentHandler.loadPunishClient(uuid);
                if (token == null) return true;

                punishmentManager.loadClient(uuid);

                PunishmentClient client = punishmentManager.punishClients.get(uuid.toLowerCase());

                for (int i = 0; i < client.getPunishments().size(); i++) {
                    Punishment punishment = client.getPunishment(uuid, PunishmentType.Ban);

                    ItemStack bans = new ItemStack(Material.REDSTONE_BLOCK);
                    ItemMeta bansmeta = bans.getItemMeta();
                    bansmeta.setDisplayName(punishment.getPunishmentType() == 0 ? "Ban" : "Mute");
                    ArrayList<String> lores = new ArrayList<>();
                    lores.add("§eReason: §f" + punishment.getReason());
                    bansmeta.setLore(lores);

                    inv.setItem(2, bans);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            plugin.getDatabaseManager().closeComponents(rs, ps, connection);
        }

        return false;
    }                                                                          
}
