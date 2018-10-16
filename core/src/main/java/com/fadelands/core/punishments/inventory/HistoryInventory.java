package com.fadelands.core.punishments.inventory;

import com.fadelands.core.Core;
import com.fadelands.core.player.User;
import com.fadelands.core.punishments.Punishment;
import com.fadelands.core.punishments.PunishmentData;
import com.fadelands.core.punishments.PunishmentType;
import com.fadelands.core.utils.ItemBuilder;
import com.fadelands.core.utils.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HistoryInventory implements Listener {

    private Core plugin;

    public HistoryInventory(Core plugin) {
        this.plugin = plugin;
    }

    private final String InvName = "Punishment History";

    public void openInventory(Player player, String target) {
        Inventory inv = Bukkit.createInventory(null,  9*5, InvName);

        updateInventory(inv, target);

        player.openInventory(inv);
    }

    public void updateInventory(Inventory inv, String target) {
        UUID uuid = UUID.fromString(User.getUuid(target));

        PunishmentData.load(uuid, (data) -> {
            if (data == null) return;

            List<Punishment> punishments = data.getPunishments();

            if (punishments.size() == 0) {
                inv.setItem(22, new ItemBuilder(Material.REDSTONE_BLOCK).setName("§c§lNo Punishments").setLore("§7Yay! No punishments were found for this user.").toItemStack());
                return;
            }

            for (int slot = 0; slot < inv.getSize() && slot < punishments.size(); slot++) {
                inv.setItem(slot, getPunishmentsDisplay(punishments.get(slot)));
            }
        });

    }

    @SuppressWarnings("Duplicates")
    private ItemStack getPunishmentsDisplay(Punishment punishment) {
        ItemBuilder builder = new ItemBuilder((punishment.isActive() ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK)).setName((punishment.getExpirationTime() == 1 ? "§6Permanent " : "§6Temporary ") + (punishment.getType() == PunishmentType.Ban ? "Ban" : "Mute"));
        List<String> lore = new ArrayList<>();

        if (punishment.isActive()) {
            lore.add("§fPunishment Type: §a" + punishment.getType().name());
            lore.add("§fPunished On: §a" + UtilTime.when(punishment.getPunishTime()));
            lore.add("§fExpires In: §a" + (punishment.isPermanent() ? "Never (Permanent)" : UtilTime.MakeStr(punishment.getRemaining())));
            lore.add("§fPunished By: §a" + User.getNameFromUuid(String.valueOf(punishment.getPunisherUuid())));
            lore.add("§r ");
            lore.add("§fReason: §a" + punishment.getReason());
            lore.add("§r ");
            lore.add("§2This punishment is still active.");
        }

        if (!punishment.isActive()) {
            lore.add("§fPunishment Type: §a" + punishment.getType().name());
            lore.add("§fPunished On: §a" + UtilTime.when(punishment.getPunishTime()));
            lore.add("§fLength: §a" + (punishment.isPermanent() ? "Permanent" : UtilTime.MakeStr(punishment.getExpirationTime())));
            lore.add("§fPunished By: §a" + User.getNameFromUuid(String.valueOf(punishment.getPunisherUuid())));
            lore.add("§r ");
            lore.add("§fReason: §a" + punishment.getReason());
        }

        if (punishment.isRemoved()) {
            lore.add("§r");
            lore.add("§fRemoved By: §a" + User.getNameFromUuid(String.valueOf(punishment.getRemoveAdmin())));
            lore.add("§fRemove Reason: §a" + punishment.getRemoveReason());
        }
        builder.setLore(lore);
        return builder.toItemStack();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getInventory().getName().equals(InvName)) {
            event.setCancelled(true);
        }
    }
}