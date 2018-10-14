package com.fadelands.core.punishments.inventory;

import com.fadelands.core.Core;
import com.fadelands.core.player.User;
import com.fadelands.core.punishments.Punishment;
import com.fadelands.core.punishments.PunishmentData;
import com.fadelands.core.utils.ItemBuilder;
import com.fadelands.core.utils.TimeUtils;
import com.fadelands.core.utils.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class HistoryInventory implements Listener {

    private Core plugin;

    public HistoryInventory(Core plugin) {
        this.plugin = plugin;
    }

    private final String InvName = "History";

    public void openInventory(Player player, String target) {
        Inventory inv = Bukkit.createInventory(null,  9*5, InvName);

        updateInventory(inv, player, target);

        player.openInventory(inv);
    }

    public void updateInventory(Inventory inv, Player player, String target) {

        UUID uuid = UUID.fromString(User.getUuid(target));

        plugin.getPunishmentManager().loadPunishments(uuid);

        PunishmentData data = plugin.getPunishmentManager().punishData.get(uuid);

        if (data.getPunishments().get(uuid) == null) {
            inv.setItem(22, new ItemBuilder(Material.REDSTONE_BLOCK).setName("§c§lNo Punishments").setLore("§7Yay! No punishments were found for this user.").toItemStack());
            return;
        }
        for (int slot = 0; slot < inv.getSize() && slot < data.getPunishments().size(); slot++) {
            inv.setItem(slot, getPunishmentsDisplay(uuid, data));
        }
    }

    @SuppressWarnings("Duplicates")
    private ItemStack getPunishmentsDisplay(UUID uuid, PunishmentData data) {
        for(Punishment punishment : data.getPunishments().get(uuid)) {
            System.out.println(punishment);

            if(punishment.isActive()) {
                ItemStack itemStack = new ItemStack(Material.EMERALD_BLOCK);
                ItemMeta itemMeta = itemStack.getItemMeta();

                itemMeta.setDisplayName(punishment.getExpirationTime() == 1 ? "§6Permanent Punishment" : "§6Temporary Punishment");

                ArrayList<String> lore = new ArrayList<>();
                lore.add("§fPunishment Type: §a" + punishment.getPunishmentType().name());
                lore.add("§fPunished On: §a" + UtilTime.when(punishment.getPunishTime()));
                lore.add("§fExpires In: §a" + (punishment.getExpirationTime() == -1 ? "Never (Permanent)" : UtilTime.MakeStr(punishment.getRemaining())));
                lore.add("§fPunished By: §a" + User.getNameFromUuid(String.valueOf(punishment.getPunisherUuid())));
                lore.add("§r ");
                lore.add("§fReason: §a" + punishment.getReason());
                lore.add("§r ");
                lore.add("§2This punishment is still active.");

                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            }

            if (!punishment.isActive()) {
                ItemStack itemStack = new ItemStack(Material.REDSTONE_BLOCK);
                ItemMeta itemMeta = itemStack.getItemMeta();

                itemMeta.setDisplayName(punishment.getExpirationTime() == 1 ? "§6Permanent Punishment" : "§6Temporary Punishment");

                ArrayList<String> lore = new ArrayList<>();
                lore.add("§fPunishment Type: §a" + punishment.getPunishmentType().name());
                lore.add("§fPunished On: §a" + UtilTime.when(punishment.getPunishTime()));
                lore.add("§fLength: §a" + (punishment.getExpirationTime() == -1 ? "Permanent" : UtilTime.MakeStr(punishment.getExpirationTime())));
                lore.add("§fPunished By: §a" + User.getNameFromUuid(String.valueOf(punishment.getPunisherUuid())));
                lore.add("§r ");
                lore.add("§fReason: §a" + punishment.getReason());

                if(punishment.isRemoved()) {
                    lore.add("§r");
                    lore.add("§fRemoved Reason: §a" + punishment.getRemoveReason());
                    lore.add("§fRemoved By: §a" + punishment.getRemoveAdmin());
                }

                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            }

        }
        return new ItemBuilder(Material.BOOK).setName("Error.").setLore("§7Well, this is awkward, hehe...", "§7Something went wrong. Come back later.").toItemStack();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getInventory().getName().equals(InvName)) {
            event.setCancelled(true);
        }
    }
}
