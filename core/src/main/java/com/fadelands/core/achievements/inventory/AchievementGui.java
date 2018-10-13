package com.fadelands.core.achievements.inventory;

import com.fadelands.core.achievements.Achievement;
import com.fadelands.core.player.User;
import com.fadelands.core.playerdata.PlayerData;
import com.fadelands.core.utils.ItemBuilder;
import com.fadelands.core.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AchievementGui implements Listener {
    private final static String inventoryName = "Achievements";

    public void openAchievements(Player player, String target) {
        if(!(User.hasPlayedBefore(target))) {
            player.sendMessage(Utils.Prefix + "§cCouldn't find that player.");
        } else{
            Inventory inv = Bukkit.createInventory(null, 9 * 6, inventoryName);

            updateInventory(inv, player, target);

            player.openInventory(inv);
        }
    }

    public void updateInventory(Inventory inv, Player player, String target) {
        UUID uuid = UUID.fromString(User.getUuid(target));
        System.out.println(uuid);
        PlayerData playerData = PlayerData.get(uuid);

        if(playerData == null) {
            return;
        }

        List<Achievement> ach = playerData.getAchivementData().getAll();

        for (int slot = 0; slot < inv.getSize() && slot < ach.size(); slot++) {
            inv.setItem(slot, getAchievementDisplay(ach.get(slot), playerData));
        }
    }

    private ItemStack getAchievementDisplay(Achievement achievement, PlayerData playerData) {
        PlayerData.AchievementData achData = playerData.getAchivementData();

        if(achData.hasCompleted(achievement)) {
            return new ItemBuilder(Material.DIAMOND_BLOCK).setName("§6" + achievement.getDisplayName()).setLore(Arrays.asList(
                    "§7Difficulty: " + achievement.getDifficulty().getDisplay(),
                    "§7Description: §a" + achievement.getDescription().get(0),
                    "§7Tokens: §a" + achievement.getDifficulty().getTokens(),
                    "§r", "§aThis achievement is unlocked.")).toItemStack();
        }

        if(!(achData.hasStarted(achievement))) {
            return new ItemBuilder(Material.COAL_BLOCK).setName("§6" + achievement.getDisplayName()).setLore(Arrays.asList(
                    "§7Difficulty: " + achievement.getDifficulty().getDisplay(),
                    "§7Description: §a" + achievement.getDescription().get(0),
                    "§7Tokens: §a" + achievement.getDifficulty().getTokens(),
                    "§r", "§cYou haven't started this achievement yet.")).toItemStack();
        }
        if(achData.hasStarted(achievement)) {
            return new ItemBuilder(Material.GOLD_BLOCK).setName("§6" + achievement.getDisplayName()).setLore(Arrays.asList(
                    "§7Difficulty: " + achievement.getDifficulty().getDisplay(),
                    "§7Description: §a" + achievement.getDescription().get(0),
                    "§7Progress: §a" + achData.getProgress(achievement) + "§7/§a" + achievement.getRequirement(),
                    "§r", "§eIn progress")).toItemStack();
        }

        return null;
    }
}