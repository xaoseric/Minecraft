package com.fadelands.core.punishments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PunishmentClient {

    private HashMap<String, List<Punishment>> punishments;

    public PunishmentClient() {
        punishments = new HashMap<>();
    }

    public void addPunishment(String uuid, Punishment punishment){
        if(!punishments.containsKey(uuid.toLowerCase())) {
            punishments.put(uuid.toLowerCase(), new ArrayList<>());
        }
        punishments.get(uuid.toLowerCase()).add(punishment);
    }

    public boolean isBanned(String uuid){
        if(!punishments.containsKey(uuid.toLowerCase())) {
            punishments.put(uuid.toLowerCase(), new ArrayList<>());
        }
        for (List<Punishment> punishments : punishments.values()) {
            for (Punishment punishment : punishments) {
                if(punishment.isBanned()){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMuted(String uuid){
        if(!punishments.containsKey(uuid.toLowerCase())) {
            punishments.put(uuid.toLowerCase(), new ArrayList<>());
        }
        for (List<Punishment> punishments : punishments.values()) {
            for (Punishment punishment : punishments) {
                if(punishment.isMuted()){
                    return true;
                }
            }
        }
        return false;
    }

    public Punishment getPunishment(String uuid, PunishmentType type){
        if(!punishments.containsKey(uuid.toLowerCase())) {
            punishments.put(uuid.toLowerCase(), new ArrayList<>());
        }
        for (List<Punishment> punishments : punishments.values()){
            for (Punishment punishment : punishments){
                if (type == PunishmentType.Ban && punishment.isBanned()){
                    return punishment;
                } else if (type == PunishmentType.Mute && punishment.isMuted()) {
                    return punishment;
                }
            }
        }

        return null;
    }

    public HashMap<String, List<Punishment>> getPunishments()
    {
        return punishments;
    }
}

