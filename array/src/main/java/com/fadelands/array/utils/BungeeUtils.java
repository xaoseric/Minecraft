package com.fadelands.array.utils;

import com.fadelands.array.Array;

public class BungeeUtils {

    public int getProxiedPlayers() {
        return new PluginMessage(Array.plugin).getPlayers("ALL");
    }

}
