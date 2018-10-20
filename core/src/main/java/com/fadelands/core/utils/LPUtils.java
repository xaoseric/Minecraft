package com.fadelands.core.utils;

import com.fadelands.core.Core;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.PermissionData;

import java.util.UUID;

public class LPUtils {

    private static LuckPermsApi luckPermsApi = Core.plugin.getLuckPermsApi();

    public static void getPrefix(UUID player) {
        luckPermsApi.getUserManager().loadUser(player).join().getCachedData().getMetaData(Contexts.global()).getPrefix();
        }
}
