package com.fadelands.core.monitor.command;

import com.fadelands.core.Core;
import com.fadelands.core.monitor.PerformanceManager;
import com.fadelands.core.player.User;
import com.fadelands.core.utils.TimeUtils;
import com.fadelands.core.utils.Utils;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

@SuppressWarnings("Duplicates")
public class MonitorCommand implements CommandExecutor {

    private Core plugin;

    public MonitorCommand(Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            useAsConsole(sender);
            return true;
        }

        Player player = (Player) sender;
        if(!(User.isRedTag(player.getName()))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        useAsPlayer(player);
        return false;
    }

    void useAsConsole(CommandSender sender) {
        PerformanceManager pm = plugin.getPerformanceManager();
        DecimalFormat format = new DecimalFormat("0.##");

        sender.sendMessage("" +
                "§8§m-===== §6Performance Monitor §8§m=====- \n" +
                "§6- Memory, Disk & System Stats \n" +
                "   §8Running on §a" + pm.getOs() + ",§8 CPU: §a" + format.format(pm.getCpuUsage()) + "\n" +
                "   §8Total server disk size: §a" + pm.getServerDiskSize() + " bytes \n" +
                "   §8RAM: §a" + pm.getTotalRam() + " MB §7(" + pm.getUsedRam() + " MB used)\n" +
                "§6- Worlds");

        for(World worlds : Bukkit.getWorlds()) {
            sender.sendMessage("" +
                    " §2" + worlds.getName() + " (" + worlds.getWorldType().getName() + "):\n" +
                    "   §8Entities: §a" + worlds.getEntities().size() + " §7(" + worlds.getLivingEntities().size() + " living) \n" +
                    "   §8Loaded chunks: §a" + worlds.getLoadedChunks().length + "\n" +
                    "   §8World folder size: §a" + pm.getFolderSize(worlds.getWorldFolder().toPath()) + " bytes \n" +
                    "   §8Spawn Rules: §a" + "Monsters [" + (!worlds.getAllowMonsters() ? "§c✖" : "§2✔") + "§a] Animals [" + (!worlds.getAllowAnimals() ? "§c✖" : "§2✔") + "§a]"); }

        sender.sendMessage("" +
                "§6- Server Stats \n" +
                "   §8Spigot version: §a" + Bukkit.getVersion() + "\n" +
                "   §8Loaded plugins: §a" + Bukkit.getPluginManager().getPlugins().length + "\n");

        StringBuilder sb = new StringBuilder("   §8TPS (1m, 5m, 15m): §a");
        for(double tps : MinecraftServer.getServer().recentTps ) {
            sb.append(format.format(tps)).append(", "); }

        sender.sendMessage(sb.substring(0, sb.length() -2) + "\n" +
                "   §8Uptime: §a" + TimeUtils.toRelative(pm.getServerUptime()) + "\n" +
                "§8§m-===== §6Performance Monitor §8§m=====-");
    }

    void useAsPlayer(Player player) {
        PerformanceManager pm = plugin.getPerformanceManager();
        DecimalFormat format = new DecimalFormat("0.##");

        player.sendMessage("" +
                "§8§m-===== §6Performance Monitor §8§m=====- \n" +
                "§6- Memory, Disk & System Stats \n" +
                "   §8Running on §a" + pm.getOs() + ",§8 CPU: §a" + format.format(pm.getCpuUsage()) + "\n" +
                "   §8Total server disk size: §a" + pm.getServerDiskSize() + " bytes \n" +
                "   §8RAM: §a" + pm.getTotalRam() + " MB §7(" + pm.getUsedRam() + " MB used)\n" +
                "§6- Worlds");

        for(World worlds : Bukkit.getWorlds()) {
            player.sendMessage("" +
                    " §2" + worlds.getName() + " (" + worlds.getWorldType().getName() + "):\n" +
                    "   §8Entities: §a" + worlds.getEntities().size() + " §7(" + worlds.getLivingEntities().size() + " living) \n" +
                    "   §8Loaded chunks: §a" + worlds.getLoadedChunks().length + "\n" +
                    "   §8World folder size: §a" + pm.getFolderSize(worlds.getWorldFolder().toPath()) + " bytes \n" +
                    "   §8Spawn Rules: §a" + "Monsters [" + (!worlds.getAllowMonsters() ? "§c✖" : "§2✔") + "§a] Animals [" + (!worlds.getAllowAnimals() ? "§c✖" : "§2✔") + "§a]"); }

        player.sendMessage("" +
                "§6- Server Stats \n" +
                "   §8Spigot version: §a" + Bukkit.getVersion() + "\n" +
                "   §8Loaded plugins: §a" + Bukkit.getPluginManager().getPlugins().length + "\n");

        StringBuilder sb = new StringBuilder("   §8TPS (1m, 5m, 15m): §a");
        for(double tps : MinecraftServer.getServer().recentTps ) {
            sb.append(format.format(tps)).append(", "); }

        player.sendMessage(sb.substring(0, sb.length() -2) + "\n" +
                "   §8Uptime: §a" + TimeUtils.toRelative(pm.getServerUptime()) + "\n" +
                "§8§m-===== §6Performance Monitor §8§m=====-");
    }
}
