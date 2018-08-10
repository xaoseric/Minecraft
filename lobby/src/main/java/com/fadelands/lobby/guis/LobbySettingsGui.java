package com.fadelands.lobby.guis;

import com.fadelands.array.Array;
import com.fadelands.lobby.Main;
import com.fadelands.lobby.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Arrays;

public class LobbySettingsGui implements Listener {

    private Main plugin;

    public LobbySettingsGui(Main plugin) {
        this.plugin = plugin;
    }

    private final static String invName = "§lYour Lobby Settings";

    private static boolean doubleJumpStatus;
    private static boolean playerVisibilityStatus;
    private static boolean speedEffectStatus;
    private static boolean jumpEffectStatus;

    private ItemStack getDoubleJump(Player player) {
        Array.executeQuery("SELECT * FROM fadelands_players_lobbysettings WHERE player_uuid='" + player.getUniqueId() + "'", resultSet -> {
            try{
                if(resultSet.next()) {
                    doubleJumpStatus = resultSet.getBoolean("double_jump");
                    resultSet.close();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }

        });
        return new ItemBuilder(Material.LEATHER_BOOTS).setName("§cDouble Jump").setLore(Arrays.asList("§r",
                "§7Currently:§e " + doubleJumpStatus)).toItemStack();
    }

    private ItemStack getPlayerVisibility(Player player) {
        Array.executeQuery("SELECT * FROM fadelands_players_lobbysettings WHERE player_uuid='" + player.getUniqueId() + "'", resultSet -> {
            try{
                if(resultSet.next()) {
                    playerVisibilityStatus = resultSet.getBoolean("player_visibility");
                    resultSet.close();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }

        });
        return new ItemBuilder(Material.WATCH).setName("§6Player Visibility").setLore(Arrays.asList("§r",
                "§7Currently:§e " + playerVisibilityStatus)).toItemStack();
    }

    private ItemStack getSpeedEffect(Player player) {
        Array.executeQuery("SELECT * FROM fadelands_players_lobbysettings WHERE player_uuid='" + player.getUniqueId() + "'", resultSet -> {
            try{
                if(resultSet.next()) {
                    speedEffectStatus = resultSet.getBoolean("speed_effect");
                    resultSet.close();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }

        });
        return new ItemBuilder(Material.POTION).setData(8194).setName("§aSpeed Effect").setLore(Arrays.asList("§r",
                "§7Currently:§e " + speedEffectStatus)).toItemStack();
    }

    private ItemStack getJumpEffect(Player player) {
        Array.executeQuery("SELECT * FROM fadelands_players_lobbysettings WHERE player_uuid='" + player.getUniqueId() + "'", resultSet -> {
            try{
                if(resultSet.next()) {
                    jumpEffectStatus = resultSet.getBoolean("jump_effect");
                    resultSet.close();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }

        });
        return new ItemBuilder(Material.POTION).setData(8203).setName("§fJump Effect").setLore(Arrays.asList("§r",
                "§7Currently:§e " + jumpEffectStatus)).toItemStack();
    }

    public void openLobbySettings(Player player) {
        Inventory selector = Bukkit.createInventory(null, 9 * 2, invName);
        selector.clear();

        selector.setItem(1, getDoubleJump(player));
        selector.setItem(3, getPlayerVisibility(player));
        selector.setItem(5, getSpeedEffect(player));
        selector.setItem(7, getJumpEffect(player));

        player.openInventory(selector);

    }

}
