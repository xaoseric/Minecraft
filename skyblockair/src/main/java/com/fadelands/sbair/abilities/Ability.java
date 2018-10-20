package com.fadelands.sbair.abilities;

import com.fadelands.sbair.Main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Ability {

    private Main plugin;

    public Ability(Main plugin) {
        this.plugin = plugin;
    }

    public enum Abilities {

        EXP_BOOST("exp-boost", "EXP Boost", "Get a 25% extra EXP Boost."),
        FARMER("farmer", "Farmer", "Replant seeds automatically and get rewards when harvesting."),
        LUMBERJACK("lumberjack", "Lumber Jack", "Chop down entire trees with only one touch!"),
        HASTE("haste", "Haste", "Get unlimited with haste wherever you go.");

        private String key;
        private String displayname;
        private List<String> desc;


        Abilities(String key, String displayname, String... desc) {
            this.key = key;
            this.displayname = displayname;
            this.desc = Arrays.asList(desc);

        }

        public static Abilities[] getAll() {
            return Abilities.values();
        }

        public String getDisplayName() {
            return displayname;
        }

        public List<String> getDescription() {
            return this.desc;
        }

        public String getKey() {
            return this.key;
        }

        public static Abilities get(String key) {
            for (Abilities ability : values()) {
                if (ability.getKey().equals(key)) return ability;
            }
            return null;
        }
    }
}