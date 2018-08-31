package com.fadelands.array.events;

import com.fadelands.array.punishments.PunishmentType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PunishedPlayerEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private UUID uuid;
    private PunishmentType type;

    public PunishedPlayerEvent(UUID uuid, PunishmentType type) {
        this.uuid = uuid;
        this.type = type;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public UUID getUuid() {
        return uuid;
    }

    public PunishmentType getPunishmentType() {
        return type;
    }

    public HandlerList getHandlers() {
        return handlers;
    }


}