package com.fadelands.sbair.abilities;

import java.util.HashMap;
import java.util.UUID;

public class AbilityData {

    private HashMap<UUID, Ability.Abilities> abilities = new HashMap<>();

    private final UUID uuid;

    public AbilityData(UUID uuid) {
        this.uuid = uuid;

        for (Ability.Abilities ability : Ability.Abilities.values()) {

            abilities.put(uuid, ability);
        }
    }

    public boolean isActive(Ability.Abilities ability) {
        return true;
    }

}
