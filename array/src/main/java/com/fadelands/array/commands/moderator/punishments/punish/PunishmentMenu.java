package com.fadelands.array.commands.moderator.punishments.punish;

import com.fadelands.array.Array;
import com.fadelands.array.commands.moderator.punishments.Punishment;
import com.fadelands.array.utils.ItemBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static com.fadelands.array.commands.moderator.punishments.punish.PunishCommandExecutor.currentPunish;

@SuppressWarnings("ALL")
public class PunishmentMenu implements Listener {

    private Array array;

    public PunishmentMenu(Array array) {
        this.array = array;
    }

    private static String invName = "§lPunish";

    Inventory inv = Bukkit.getServer().createInventory(null, 9*6, invName);
    public void openAsAdmin(Player player, String target, String reason) {
        inv.clear();
        //first row
        inv.setItem(0, barrier);
        inv.setItem(1, barrier);
        inv.setItem(2, getChatOffense);
        inv.setItem(3, barrier);
        inv.setItem(4, getGameplayOffense);
        inv.setItem(5, barrier);
        inv.setItem(6, getClientOffense);
        inv.setItem(7, barrier);
        inv.setItem(8, barrier);
        //second row
        inv.setItem(11, chatOffenseSevOne);
        inv.setItem(13, gameplayOffenseSevOne);
        inv.setItem(15, clientOffenseSevTwo);
        //third row
        inv.setItem(20, chatOffenseSevTwo);
        inv.setItem(22, gameplayOffenseSevTwo);
        inv.setItem(24, clientOffenseSevThree);
        //fourth row
        inv.setItem(29, chatOffenseSevThree);
        inv.setItem(31, gameplayOffenseSevThree);
        //fifth row
        inv.setItem(38, chatOffenseSevFour);
        inv.setItem(42, unmute);
        inv.setItem(43, unban);
        //sixth row
        inv.setItem(45, barrier);
        inv.setItem(46, barrier);
        inv.setItem(47, barrier);
        inv.setItem(48, barrier);
        inv.setItem(49, barrier);
        inv.setItem(50, barrier);
        inv.setItem(51, barrier);
        inv.setItem(52, barrier);
        inv.setItem(53, barrier);
        //first side
        inv.setItem(9, getTargetHead(target, reason));
        inv.setItem(18, getHistory);
        inv.setItem(27, getAlternativeAccounts);
        inv.setItem(36, barrier);
        //last side
        inv.setItem(17, permIPBan);
        inv.setItem(26, permBan);
        inv.setItem(35, permMute);
        inv.setItem(44, barrier);

        player.openInventory(inv);
    }
    public void openAsSenior(Player player, String target, String reason) {
        inv.clear();
        //first row
        inv.setItem(0, barrier);
        inv.setItem(1, barrier);
        inv.setItem(2, getChatOffense);
        inv.setItem(3, barrier);
        inv.setItem(4, getGameplayOffense);
        inv.setItem(5, barrier);
        inv.setItem(6, getClientOffense);
        inv.setItem(7, barrier);
        inv.setItem(8, barrier);
        //second row
        inv.setItem(11, chatOffenseSevOne);
        inv.setItem(13, gameplayOffenseSevOne);
        inv.setItem(15, clientOffenseSevTwo);
        //third row
        inv.setItem(20, chatOffenseSevTwo);
        inv.setItem(22, gameplayOffenseSevTwo);
        inv.setItem(24, clientOffenseSevThree);
        //fourth row
        inv.setItem(29, chatOffenseSevThree);
        inv.setItem(31, gameplayOffenseSevThree);
        //fifth row
        inv.setItem(38, chatOffenseSevFour);
        inv.setItem(42, unmute);
        inv.setItem(43, unban);
        //sixth row
        inv.setItem(45, barrier);
        inv.setItem(46, barrier);
        inv.setItem(47, barrier);
        inv.setItem(48, barrier);
        inv.setItem(49, barrier);
        inv.setItem(50, barrier);
        inv.setItem(51, barrier);
        inv.setItem(52, barrier);
        inv.setItem(53, barrier);
        //first side
        inv.setItem(9, getTargetHead(target, reason));
        inv.setItem(18, getHistory);
        inv.setItem(27, getAlternativeAccounts);
        inv.setItem(36, barrier);
        //last side
        inv.setItem(17, barrier);
        inv.setItem(26, permBan);
        inv.setItem(35, barrier);
        inv.setItem(44, barrier);

        player.openInventory(inv);
    }
    public void openAsMod(Player player, String target, String reason) {
        inv.clear();
        //first row
        inv.setItem(0, barrier);
        inv.setItem(1, barrier);
        inv.setItem(2, getChatOffense);
        inv.setItem(3, barrier);
        inv.setItem(4, getGameplayOffense);
        inv.setItem(5, barrier);
        inv.setItem(6, getClientOffense);
        inv.setItem(7, barrier);
        inv.setItem(8, barrier);
        //second row
        inv.setItem(11, chatOffenseSevOne);
        inv.setItem(13, gameplayOffenseSevOne);
        inv.setItem(15, clientOffenseSevTwo);
        //third row
        inv.setItem(20, chatOffenseSevTwo);
        inv.setItem(22, gameplayOffenseSevTwo);
        inv.setItem(24, clientOffenseSevThree);
        //fourth row
        inv.setItem(29, chatOffenseSevThree);
        inv.setItem(31, gameplayOffenseSevThree);
        //fifth row
        inv.setItem(38, chatOffenseSevFour);
        inv.setItem(42, unmute);
        inv.setItem(43, unban);
        //sixth row
        inv.setItem(45, barrier);
        inv.setItem(46, barrier);
        inv.setItem(47, barrier);
        inv.setItem(48, barrier);
        inv.setItem(49, barrier);
        inv.setItem(50, barrier);
        inv.setItem(51, barrier);
        inv.setItem(52, barrier);
        inv.setItem(53, barrier);
        //first side
        inv.setItem(9, getTargetHead(target, reason));
        inv.setItem(18, getHistory);
        inv.setItem(27, barrier);
        inv.setItem(36, barrier);
        //last side
        inv.setItem(17, barrier);
        inv.setItem(26, permBan);
        inv.setItem(35, barrier);
        inv.setItem(44, barrier);

        player.openInventory(inv);
    }
    public void openAsTrainee(Player player, String target, String reason) {
        inv.clear();
        //first row
        inv.setItem(0, barrier);
        inv.setItem(1, barrier);
        inv.setItem(2, getChatOffense);
        inv.setItem(3, barrier);
        inv.setItem(4, getGameplayOffense);
        inv.setItem(5, barrier);
        inv.setItem(6, getClientOffense);
        inv.setItem(7, barrier);
        inv.setItem(8, barrier);
        //second row
        inv.setItem(11, chatOffenseSevOne);
        inv.setItem(13, gameplayOffenseSevOne);
        inv.setItem(15, clientOffenseSevTwo);
        //third row
        inv.setItem(20, chatOffenseSevTwo);
        inv.setItem(22, gameplayOffenseSevTwo);
        //fourth row

        //fifth row
        inv.setItem(43, unmute);
        //sixth row
        inv.setItem(45, barrier);
        inv.setItem(46, barrier);
        inv.setItem(47, barrier);
        inv.setItem(48, barrier);
        inv.setItem(49, barrier);
        inv.setItem(50, barrier);
        inv.setItem(51, barrier);
        inv.setItem(52, barrier);
        inv.setItem(53, barrier);
        //first side
        inv.setItem(9, getTargetHead(target, reason));
        inv.setItem(18, getHistory);
        inv.setItem(27, barrier);
        inv.setItem(36, barrier);
        //last side
        inv.setItem(17, barrier);
        inv.setItem(26, barrier);
        inv.setItem(35, barrier);
        inv.setItem(44, barrier);

        player.openInventory(inv);
    }
    private ItemStack getTargetHead(String target, String reason) {
        return new ItemBuilder(Material.SKULL_ITEM).setData(3).setSkullOwner(target).setName("§a§l" + target).setLore("§7Reason§f: " + reason).toItemStack();
    }

    private ItemStack getAlternativeAccounts = new ItemBuilder(Material.REDSTONE_COMPARATOR).setName("§c§l" + "Alt Accounts").setLore("§7Click to view alt accounts.").toItemStack();
    private ItemStack getHistory = new ItemBuilder(Material.MAP).setName("§e§l" + "Punishm. History").setLore("§7Click to view targets punishments history.").toItemStack();
    private ItemStack getChatOffense = new ItemBuilder(Material.BOOK_AND_QUILL).setName("§a§lChat Offenses").setLore(Arrays.asList("§7Spam, Harrassment, Toxic Behavior", "§7Verbal Abuse, etc.")).toItemStack();
    private ItemStack getGameplayOffense = new ItemBuilder(Material.ARROW).setName("§a§lGameplay Offenses").setLore("§7Game Exploits, Bug Abusing, etc.").toItemStack();
    private ItemStack getClientOffense = new ItemBuilder(Material.IRON_SWORD).setName("§a§lClient Offenses").setLore(Arrays.asList("§7Usage of illegal modifications", "§7hacked clients, etc.")).toItemStack();

    private ItemStack chatOffenseSevOne = new ItemBuilder(Material.STAINED_CLAY).setData(5).setName("§a§lSeverity 1").setLore(Arrays.asList("§fMute Duration: §b1 Day", "§r ",
            "§7E.g: Spamming, Light Advertisement, etc.","§r ","§a>§2>§a> Click to punish.")).toItemStack();

    private ItemStack chatOffenseSevTwo = new ItemBuilder(Material.STAINED_CLAY).setData(4).setName("§6§lSeverity 2").setLore(Arrays.asList("§fMute Duration: §b7 Days", "§r ",
            "§7E.g: Harassment, Abusive Behavior, Innap. Links, etc.","§r ","§a>§2>§a> Click to punish.")).toItemStack();

    private ItemStack chatOffenseSevThree = new ItemBuilder(Material.STAINED_CLAY).setData(14).setName("§c§lSeverity 3").setLore(Arrays.asList("§fMute Duration: §b14 Days", "§r ",
            "§7E.g: Racism, Discrimination, Sharing personal info, etc.","§r ","§a>§2>§a> Click to punish.")).toItemStack();

    private ItemStack chatOffenseSevFour = new ItemBuilder(Material.STAINED_CLAY).setData(14).setName("§4§lSeverity 4").setLore(Arrays.asList("§fMute Duration: §b30 Days", "§r ",
            "§7E.g: Advertisement, Unapproved Transactions, Malicious Threats, etc.","§r ","§a>§2>§a> Click to punish.")).toItemStack();

    private ItemStack gameplayOffenseSevOne = new ItemBuilder(Material.STAINED_CLAY).setData(5).setName("§a§lSeverity 1").setLore(Arrays.asList("§fBan Duration: §b1 Day", "§r ",
            "§7E.g: Map/Bug Exploiting, etc.","§r ","§a>§2>§a> Click to punish.")).toItemStack();

    private ItemStack gameplayOffenseSevTwo = new ItemBuilder(Material.STAINED_CLAY).setData(4).setName("§6§lSeverity 2").setLore(Arrays.asList("§fBan Duration: §b7 Days", "§r ",
            "§7E.g: Abusing Bugs/Glitches, etc.","§r ","§a>§2>§a> Click to punish.")).toItemStack();

    private ItemStack gameplayOffenseSevThree = new ItemBuilder(Material.STAINED_CLAY).setData(14).setName("§c§lSeverity 3").setLore("§7No punishments uses this severity yet.").toItemStack();

    private ItemStack clientOffenseSevTwo = new ItemBuilder(Material.STAINED_CLAY).setData(4).setName("§6§lSeverity 2").setLore(Arrays.asList("§fBan Duration: §b30 Days", "§r ",
            "§7E.g: Using Illegal Modifications, etc.","§r ","§a>§2>§a> Click to punish.")).toItemStack();

    private ItemStack clientOffenseSevThree = new ItemBuilder(Material.STAINED_CLAY).setData(14).setName("§c§lSeverity 3").setLore(Arrays.asList("§fBan Duration: §bPERMANENT", "§r ",
            "§7E.g: Speed, Kill-Aura, Flight, B-hop, etc.","§r ","§a>§2>§a> Click to punish.")).toItemStack();

    private ItemStack permBan = new ItemBuilder(Material.REDSTONE_BLOCK).setName("§c§lPermanent Ban").setLore(Arrays.asList("§fBan Duration: §bPERMANENT", "§r ",
            "§2Requires detailed reason.","§r ","§a>§2>§a> Click to punish.")).toItemStack();

    private ItemStack permMute = new ItemBuilder(Material.BOOK_AND_QUILL).setName("§c§lPermanent Mute").setLore(Arrays.asList("§fMute Duration: §bPERMANENT", "§r ",
            "§2Requires detailed reason.","§r ","§a>§2>§a> Click to punish.")).toItemStack();

    private ItemStack permIPBan = new ItemBuilder(Material.BARRIER).setName("§c§lPermanent IP Ban").setLore(Arrays.asList("§fBan Duration: §bPERMANENT", "§r ",
            "§2Requires detailed reason.","§r ","§a>§2>§a> Click to punish.")).toItemStack();

    private ItemStack unban = new ItemBuilder(Material.ANVIL).setName("§a§lUnban").setLore("§7Click to unban user.").toItemStack();
    private ItemStack unmute = new ItemBuilder(Material.PAPER).setName("§a§lUnmute").setLore("§7Click to unmute user.").toItemStack();

    private ItemStack barrier = new ItemBuilder(Material.STAINED_GLASS_PANE).setData(7).setName(" ").toItemStack();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        Punishment punishment = currentPunish.get(event.getWhoClicked().getUniqueId());
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(invName)) {
            if (!currentPunish.containsKey(event.getWhoClicked().getUniqueId())) {
                event.getWhoClicked().sendMessage("Could not find any data regarding the punishments...");
                event.getWhoClicked().closeInventory();
                return;
            }
            event.setCancelled(true);

            if(event.getCurrentItem().isSimilar(getHistory)){
                player.closeInventory();
                String command = "/history " + punishment.getTargetName();
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
            if(event.getCurrentItem().isSimilar(getAlternativeAccounts)){
                player.closeInventory();
                String command = "/dupeip " + punishment.getTargetName();
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
            if(event.getCurrentItem().isSimilar(chatOffenseSevOne)){
                player.closeInventory();
                String command = "/mute -s " + punishment.getTargetName() + " 1d " + punishment.getReason().getMessage();;
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
            if(event.getCurrentItem().isSimilar(chatOffenseSevTwo)){
                player.closeInventory();
                String command = "/mute -s " + punishment.getTargetName() + " 7d " + punishment.getReason().getMessage();;
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
            if(event.getCurrentItem().isSimilar(chatOffenseSevThree)){
                player.closeInventory();
                String command = "/mute -s " + punishment.getTargetName() + " 14d " + punishment.getReason().getMessage();;
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
            if(event.getCurrentItem().isSimilar(chatOffenseSevFour)){
                player.closeInventory();
                String command = "/mute -s " + punishment.getTargetName() + " 30d " + punishment.getReason().getMessage();;
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
            if(event.getCurrentItem().isSimilar(gameplayOffenseSevOne)){
                player.closeInventory();
                String command = "/ban -s " + punishment.getTargetName() + " 1d " + punishment.getReason().getMessage();;
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
            if(event.getCurrentItem().isSimilar(gameplayOffenseSevTwo)){
                player.closeInventory();
                String command = "/ban -s " + punishment.getTargetName() + " 7d " + punishment.getReason().getMessage();;
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
            if(event.getCurrentItem().isSimilar(clientOffenseSevTwo)){
                player.closeInventory();
                String command = "/ban -s " + punishment.getTargetName() + " 30d " + punishment.getReason().getMessage();;
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
            if(event.getCurrentItem().isSimilar(clientOffenseSevThree)){
                player.closeInventory();
                String command = "/ban -s " + punishment.getTargetName() + " " + punishment.getReason().getMessage();
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
            if(event.getCurrentItem().isSimilar(permBan)){
                player.closeInventory();
                String command = "/ban -s " + punishment.getTargetName() + " " + punishment.getReason().getMessage();;
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
            if(event.getCurrentItem().isSimilar(permMute)){
                player.closeInventory();
                String command = "/mute -s " + punishment.getTargetName() + " " + punishment.getReason().getMessage();;
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
            if(event.getCurrentItem().isSimilar(permIPBan)){
                player.closeInventory();
                String command = "/ipban -s " + punishment.getTargetName() + " " + punishment.getReason().getMessage();;
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
            if(event.getCurrentItem().isSimilar(unban)){
                player.closeInventory();
                String command = "/unban -s " + punishment.getTargetName();
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
            if(event.getCurrentItem().isSimilar(unmute)){
                player.closeInventory();
                String command = "/unmute -s " + punishment.getTargetName();
                ComponentBuilder c = new ComponentBuilder("§a§l>§2§l>§a§l> Press to confirm and execute the command.");
                c.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                c.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(command).create()));
                player.spigot().sendMessage(c.create());
            }
         }
        }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
            if (currentPunish.containsKey(event.getPlayer().getUniqueId()))
                currentPunish.remove(event.getPlayer().getUniqueId());
        }
    }