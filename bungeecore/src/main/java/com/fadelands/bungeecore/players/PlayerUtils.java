package com.fadelands.bungeecore.players;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerUtils {

    public static boolean playerIsStaff(ProxiedPlayer player){
        if(player.hasPermission("fadelands.staff")){

        }
        return true;
    }
}
