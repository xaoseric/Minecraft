package com.fadelands.array.database;

import com.zaxxer.hikari.HikariPoolMXBean;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class DatabasePerformance {

    private static HikariPoolMXBean poolProxy;
    private int idleConnections;

    public DatabasePerformance() {
        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName poolName = new ObjectName("com.zaxxer.hikari:type=Pool (flarray)");
            poolProxy = JMX.newMXBeanProxy(mBeanServer, poolName, HikariPoolMXBean.class);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
    }

    public static Integer getIdleConnections() {
        int idleConnections;
        idleConnections = poolProxy.getIdleConnections();
        return idleConnections;
    }

    public static Integer getActiveConnections() {
        int activeConnections;
        activeConnections = poolProxy.getActiveConnections();
        return activeConnections;
    }

    public static Integer getTotalConnections() {
        int totalConnections;
        totalConnections = poolProxy.getTotalConnections();
        return totalConnections;
    }

    public static Integer getWaitingThreads() {
        int waitingThreads;
        waitingThreads = poolProxy.getThreadsAwaitingConnection();
        return waitingThreads;
    }

    public static String getObjectName() {
        return "flarray";
    }
}
