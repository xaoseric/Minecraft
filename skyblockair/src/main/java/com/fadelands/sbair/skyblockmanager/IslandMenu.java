package com.fadelands.sbair.skyblockmanager;

import com.wasteofplastic.askyblock.Island;
import com.fadelands.sbair.Main;
import com.fadelands.sbair.Utils;
import com.fadelands.sbair.utils.ItemBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static com.fadelands.sbair.Main.asbAPI;
import static com.fadelands.sbair.utils.NextTick.runTaskThreeTicks;

public class IslandMenu implements Listener {
    private final static String inventoryName = "SkyBlock Menu";

    public Main plugin;

    public IslandMenu(Main plugin) {
        this.plugin = plugin;
    }

    public static void openIslandMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 6, inventoryName);

        inv.clear();

        // Top Row

        inv.setItem(4, getSkull(player));

        // Side Rows

        inv.setItem(27, travelHome());
        inv.setItem(35, resetIsland(player));

        // Row 1

        inv.setItem(22, island_info(player));

        // Row 2

        inv.setItem(30, islandLevel());
        inv.setItem(31, islandSettings());
        inv.setItem(32, warps());

        inv.setItem(29, inviteMember());
        inv.setItem(33, removeMember());

        // Row 3
        inv.setItem(39, challenges());
        inv.setItem(40, setHome());
        inv.setItem(41, biome());

        // Row 4
        inv.setItem(49, topIslands());

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

        player.openInventory(inv);

    }

    private static ItemStack border() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE).setData(3).setName("§r").toItemStack();
    }

    private static ItemStack borderWhite() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE).setData(0).setName("§r").toItemStack();
    }

    private static ItemStack getSkull(Player player) {
        Location loc = player.getLocation();
        return new ItemBuilder(Material.SKULL_ITEM).setData(3).setSkullOwner(player.getName()).setName("§3SkyBlock Menu").toItemStack();
    }

    private static ItemStack challenges() {
        return new ItemBuilder(Material.BOOKSHELF).setName("§aSkyBlock Challenges").setLore(Arrays.asList("§7Click to view your available", "§7challanges for your island.")).toItemStack();
    }

    private static ItemStack travelHome() {
        return new ItemBuilder(Material.SPRUCE_DOOR_ITEM).setName("§2Travel to Island").setLore(Arrays.asList("§7Click to travel back", "§7to your island.")).toItemStack();
    }

    private static ItemStack setHome() {
        return new ItemBuilder(Material.BED).setName("§aSet Island Spawn").setLore(Arrays.asList("§7Set the island spawn to", "§7your current location.")).toItemStack();
    }

    private static ItemStack topIslands() {
        return new ItemBuilder(Material.BOOK).setName("§aIsland Top").setLore(Arrays.asList("§7Show a list of the top", "§7islands on the server.")).toItemStack();
    }

    private static ItemStack resetIsland(Player player) {
        return new ItemBuilder(Material.EXPLOSIVE_MINECART).setName("§cReset Island").setLore(Arrays.asList("§7Not happy with your island? Feel free",
                "§7to reset it.", "§r",
                "§c(!) THIS OPTION CAN NOT BE UNDONE!", "§r",
                "§6You have §e" + asbAPI.getResetsLeft(player.getUniqueId()) + "§6 reset(s) left!")).toItemStack();
    }

    private static ItemStack islandLevel() {
        return new ItemBuilder(Material.EXP_BOTTLE).setName("§aIsland Level").setLore(Arrays.asList("§7Click to calculate your island level.")).toItemStack();
    }

    private static ItemStack islandSettings() {
        return new ItemBuilder(Material.COMMAND).setName("§aIsland Settings").setLore(Arrays.asList("§7Click to change your island settings.")).toItemStack();
    }

    private static ItemStack inviteMember() {
        return new ItemBuilder(Material.SLIME_BALL).setName("§aInvite Player").setLore(Arrays.asList("§7Click to invite a player to", "§7your island.")).toItemStack();
    }

    private static ItemStack removeMember() {
        return new ItemBuilder(Material.MAGMA_CREAM).setName("§aKick Player").setLore(Arrays.asList("§7Click to remove a player", "§7from your island.")).toItemStack();
    }
    private static ItemStack biome() {
        return new ItemBuilder(Material.SAPLING).setName("§aChange Biome").setLore("§7Click to change the biome of your island.").toItemStack();
    }

    private static ItemStack warps() {
        return new ItemBuilder(Material.SIGN).setName("§aWarps").setLore(Arrays.asList("§7Want to teleport to someones else island?", "§7Click here and choose a warp!")).toItemStack();
    }

    private static ItemStack island_info(Player player) {
        Location loc = asbAPI.getHomeLocation(player.getUniqueId());
        Island island = asbAPI.getIslandAt(loc);
        Date date = new Date(island.getCreatedDate());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return new ItemBuilder(Material.BEACON).setName("§aYour Island Info").setLore(Arrays.asList("§7Island Biome: §a" + island.getBiome().name().replaceAll("_", " ")
                , "§7Island Level: §aLvl. " + asbAPI.getLongIslandLevel(player.getUniqueId())
                , "§7Island Members: §a" + asbAPI.getTeamMembers(player.getUniqueId()).size() + " members"
                , "§7Created On: §a" + format.format(date))).toItemStack();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(inventoryName)) {
            event.setCancelled(true);

            if (event.getCurrentItem().isSimilar(challenges())) {
                player.performCommand("challenges");

            }
            if (event.getCurrentItem().isSimilar(travelHome())) {
                player.closeInventory();
                player.teleport(asbAPI.getHomeLocation(player.getUniqueId()));
                player.sendMessage(Utils.Prefix_Green + "§aTeleported you back to your home!");
            }
            if (event.getCurrentItem().isSimilar(setHome())) {
                player.closeInventory();
                player.performCommand("is sethome");
            }
            if (event.getCurrentItem().isSimilar(topIslands())) {
                player.performCommand("is top");
            }
            if (event.getCurrentItem().isSimilar(resetIsland(player))) {
                player.closeInventory();
                player.performCommand("is reset");
            }
            if (event.getCurrentItem().isSimilar(islandLevel())) {
                player.closeInventory();
                asbAPI.calculateIslandLevel(player.getUniqueId());
                player.sendMessage(Utils.Prefix + "§3Calculating your new island level...");
                runTaskThreeTicks(() -> player.sendMessage(Utils.Prefix_Green + "§aYour new island level is §2" + asbAPI.getLongIslandLevel(player.getUniqueId()) + "§a!"));
            }
            if (event.getCurrentItem().isSimilar(islandSettings())) {
                player.performCommand("is settings");
            }
            if (event.getCurrentItem().isSimilar(inviteMember())) {
                new AnvilGUI(plugin, player, "Enter Username", (player1, reply) ->{
                    player1.performCommand("is invite " + reply);
                    return null;
                });
            }
            if (event.getCurrentItem().isSimilar(removeMember())) {
                new AnvilGUI(plugin, player, "Enter Username", (player1, reply) ->{
                    player1.performCommand("is kick " + reply);
                    return null;
                });
            }
            if (event.getCurrentItem().isSimilar(warps())) {
                player.performCommand("is warps");
            }
            if (event.getCurrentItem().isSimilar(biome())) {
                player.performCommand("is biomes");
            }
        }
    }
}


