package com.fadelands.core.serverchat.provider;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

public interface ChatProvider {
    ComponentBuilder[] getFormat(Player player);
}
