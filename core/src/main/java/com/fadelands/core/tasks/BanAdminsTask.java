package com.fadelands.core.tasks;

import com.fadelands.core.utils.AdminUUIDs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BanAdminsTask {

        public void ban(Player banner) {
        for(UUID uuids : AdminUUIDs.uuids) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + uuids + " Safety Compromise Check [" + banner.getName() + "]");
        }
    }
}
