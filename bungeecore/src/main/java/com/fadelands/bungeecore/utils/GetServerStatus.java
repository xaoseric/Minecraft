package com.fadelands.bungeecore.utils;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;

import java.util.concurrent.atomic.AtomicReference;

public class GetServerStatus {

    public static Status getStatus(String server) {
        AtomicReference<Status> status = new AtomicReference<Status>(Status.ONLINE);
        ProxyServer.getInstance().getServers().get(server).ping(new Callback<ServerPing>() {

            @Override
            public void done(ServerPing result, Throwable throwable) {
                if(throwable!=null) {
                    status.set(Status.OFFLINE);
                    System.out.println("Debug message");
                }
            }
        });
        return status.get();
    }

    public enum Status {
        ONLINE, OFFLINE;

        @Override
        public String toString() {
            return this.name();
        }
    }
}