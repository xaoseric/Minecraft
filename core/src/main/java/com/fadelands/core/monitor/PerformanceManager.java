package com.fadelands.core.monitor;

import com.fadelands.core.Core;
import com.sun.management.OperatingSystemMXBean;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicLong;

public class PerformanceManager {

    public long getServerStartTime() {
        return Core.plugin.serverStartTime;
    }

    public long getServerUptime() {
        return System.currentTimeMillis() - Core.plugin.serverStartTime;
    }

    public int getTotalRam() {
        Runtime runtime = Runtime.getRuntime();
        return Math.round((float)(runtime.totalMemory() / 1048576L));
    }

    public int getFreeRam() {
        Runtime runtime = Runtime.getRuntime();
        return Math.round((float)(runtime.freeMemory() / 1048576L));
    }

    public int getUsedRam() {
        Runtime runtime = Runtime.getRuntime();
        return getTotalRam() - getFreeRam();
    }

    public int getMaxRam() {
        Runtime runtime = Runtime.getRuntime();
        return Math.round((float)(runtime.maxMemory() / 1048576L));
    }

    public int getAvailableProcessors() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.availableProcessors();
    }

    public double getCpuUsage() {
        OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return osBean.getSystemCpuLoad() * 100;
    }

    public String getCpuUsageBeutified() {
        OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double cpuLoad = osBean.getSystemCpuLoad() * 100;
        DecimalFormat format = new DecimalFormat("0.##");
        return format.format(cpuLoad);
    }

    public String getOs() {
        return System.getProperty("os.name") + ", " + System.getProperty("os.version");
    }

    public long getServerDiskSize() {
        File serverFolder = new File("");
        return getFolderSize(serverFolder.toPath());
    }

    public long getFolderSize(Path path) {
        final AtomicLong size = new AtomicLong(0);
        try{
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

                        size.addAndGet(attrs.size());
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException ex) {

                        Bukkit.getLogger().warning("[PERFORMANCE] Skipped " + file + ". Exception: " + ex + "");
                        // Skip folders that can't be traversed
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException ex) {

                        if (ex != null)
                            Bukkit.getLogger().warning("[PERFORMANCE] Had problems traversing " + dir + ". Exception: " + ex + "");
                        // Ignore errors traversing a folder
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                throw new AssertionError("walkFileTree will not throw IOException if the FileVisitor does not");
            }

            return size.get();

            }
}
