package com.fadelands.core.utils;

import com.fadelands.core.Core;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;

import java.util.UUID;
import java.util.function.Consumer;

public class LPUtils {

    private static LuckPermsApi luckPermsApi = Core.plugin.getLuckPermsApi();

    public static void getUser(UUID player, Consumer<? super User> callback) {
        luckPermsApi.getUserManager().loadUser(player).thenAccept(callback);
    }
}
