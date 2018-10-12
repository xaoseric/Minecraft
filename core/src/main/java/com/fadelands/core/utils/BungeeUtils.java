package com.fadelands.core.utils;

import com.fadelands.core.Core;

public class BungeeUtils {

    public int getProxiedPlayers() {
        return new PluginMessage(Core.plugin).getPlayers("ALL");
    }

}
