package com.fadelands.sbair.skyblockmanager;

import com.fadelands.core.utils.ItemBuilder;
import com.fadelands.core.utils.Utils;
import com.fadelands.sbair.Main;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Date;

public class IslandMenu implements Listener {

    private final static String inventoryName = "SkyBlock Menu";

    public Main plugin;

    public IslandMenu(Main plugin) {
        this.plugin = plugin;
    }

    public void openIslandMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 6, inventoryName);

        updateInventory(inv, player);

        player.openInventory(inv);

    }

    private static ItemStack border() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE).setData(3).setName("§r").toItemStack();
    }

    private static ItemStack borderWhite() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE).setData(0).setName("§r").toItemStack();

    }

    public void updateInventory(Inventory inv, Player player) {

        inv.setItem(4, new ItemBuilder(Material.SKULL_ITEM).setData(3).setSkullOwner(player.getName()).setName("§6SkyBlock Menu").toItemStack());

        // Side Rows

        inv.setItem(27, new ItemBuilder(Material.SPRUCE_DOOR_ITEM).setName("§2Travel to Island").setLore(Arrays.asList("§7Click to travel back", "§7to your island.")).toItemStack());
        inv.setItem(35, new ItemBuilder(Material.EXPLOSIVE_MINECART).setName("§cReset Island").setLore(Arrays.asList("§7Not happy with your island? Feel free",
                "§7to reset it.", "§r",
                "§c(!) THIS OPTION CAN NOT BE UNDONE!", "§r",
                "§6You have §e" + plugin.getSkyBlockApi().getResetsLeft(player.getUniqueId()) + "§6 reset(s) left!")).toItemStack());

        // Row 1

        inv.setItem(22, new ItemBuilder(Material.BEACON).setName("§aYour Island Info").setLore(Arrays.asList("§7Island Biome: §a" + plugin.getSkyBlockApi().getIslandAt(player.getLocation()).getBiome().name().replaceAll("_", " ")
                , "§7Island Level: §aLvl. " + plugin.getSkyBlockApi().getLongIslandLevel(player.getUniqueId())
                , "§7Island Members: §a" + plugin.getSkyBlockApi().getTeamMembers(player.getUniqueId()).size() + " members"
                , "§7Created On: §a" + new Date(plugin.getSkyBlockApi().getIslandAt(player.getLocation()).getCreatedDate()))).toItemStack());

        // Row 2

        inv.setItem(30, new ItemBuilder(Material.EXP_BOTTLE).setName("§aIsland Level").setLore("§7Click to calculate your island level.").toItemStack());
        inv.setItem(31, new ItemBuilder(Material.COMMAND).setName("§aIsland Settings").setLore("§7Click to change your island settings.").toItemStack());
        inv.setItem(32, new ItemBuilder(Material.SIGN).setName("§aWarps").setLore(Arrays.asList("§7Want to teleport to someones else island?", "§7Click here and choose a warp!")).toItemStack());

        inv.setItem(29, new ItemBuilder(Material.SLIME_BALL).setName("§aInvite Player").setLore(Arrays.asList("§7Click to invite a player to", "§7your island.")).toItemStack());
        inv.setItem(33, new ItemBuilder(Material.MAGMA_CREAM).setName("§aKick Player").setLore(Arrays.asList("§7Click to remove a player", "§7from your island.")).toItemStack());

        // Row 3
        inv.setItem(39, new ItemBuilder(Material.BOOKSHELF).setName("§aSkyBlock Challenges").setLore(Arrays.asList("§7Click to view your available", "§7challanges for your island.")).toItemStack());
        inv.setItem(40, new ItemBuilder(Material.BED).setName("§aSet Island Spawn").setLore(Arrays.asList("§7Set the island spawn to", "§7your current location.")).toItemStack());
        inv.setItem(41,new ItemBuilder(Material.SAPLING).setName("§aChange Biome").setLore("§7Click to change the biome of your island.").toItemStack());

        // Row 4
        inv.setItem(49, new ItemBuilder(Material.BOOK).setName("§aIsland Top").setLore(Arrays.asList("§7Show a list of the top", "§7islands on the server.")).toItemStack());

        //border rows
        inv.setItem(9, border());
        inv.setItem(10, border());
        inv.setItem(11, border());
        inv.setItem(12, border());
        inv.setItem(13, border());
        inv.setItem(14, border());
        inv.setItem(15, border());
        inv.setItem(16, border());
        inv.setItem(17, border());

        inv.setItem(53, border());
        inv.setItem(52, border());
        inv.setItem(51, border());
        inv.setItem(50, border());
        inv.setItem(48, border());
        inv.setItem(47, border());
        inv.setItem(46, border());
        inv.setItem(45, border());

        inv.setItem(19, borderWhite());
        inv.setItem(25, borderWhite());
        inv.setItem(28, borderWhite());
        inv.setItem(34, borderWhite());
        inv.setItem(37, borderWhite());
        inv.setItem(43, borderWhite());

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(inventoryName)) {
            event.setCancelled(true);

            switch (event.getSlot()) {

                case 27:
                    player.closeInventory();
                    player.teleport(plugin.getSkyBlockApi().getHomeLocation(player.getUniqueId()));
                    player.sendMessage(Utils.Prefix_Green + "§aTeleported you back to your home!");
                    break;

                case 35:
                    player.closeInventory();
                    player.performCommand("is reset");
                    break;

                case 29:
                    new AnvilGUI(plugin, player, "Enter Username", (player1, reply) ->{
                        player1.performCommand("is invite " + reply);
                        return null;
                    });
                    break;

                case 30:
                    player.closeInventory();
                    plugin.getSkyBlockApi().calculateIslandLevel(player.getUniqueId());
                    player.sendMessage(Utils.Prefix + "§aCalculating your new island level...");
                    player.sendMessage(Utils.Prefix_Green + "§aYour new island level is §2" + plugin.getSkyBlockApi().getLongIslandLevel(player.getUniqueId()) + "§a!");
                    break;

                case 31:
                    player.performCommand("is settings");
                    break;

                case 32:
                    player.performCommand("is warps");
                    break;

                case 33:
                    new AnvilGUI(plugin, player, "Enter Username", (player1, reply) ->{
                        player1.performCommand("is kick " + reply);
                        return null;
                    });
                    break;

                case 39:
                    player.performCommand("challenges");
                    break;

                case 40:
                    player.performCommand("is sethome");
                    break;

                case 41:
                    player.performCommand("is biomes");
                    break;

                case 49:
                    player.performCommand("is top");
                    break;
            }
        }
    }
}


