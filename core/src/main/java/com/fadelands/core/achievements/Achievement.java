package com.fadelands.core.achievements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Achievement {

    private static final List<Achievement> registered = new ArrayList<>();


    //Chat Achievements
    public static final Achievement FIRST_WORDS = new Achievement("first-words", "First Words", DifficultLevel.EASY, "Talk in the chat for the first time");
    public static final Achievement FIRST_COMMAND = new Achievement("first-command", "What does this do?", DifficultLevel.EASY, "Use a command for the first time");

    // Killing Achievements
    public static final Achievement SERIAL_KILLER_I = new Achievement("serial-killer-I", "Serial Killer I", DifficultLevel.NORMAL, 100, "Get a total of 100 kills");
    public static final Achievement SERIAL_KILLER_II = new Achievement("serial-killer-II", "Serial Killer II", DifficultLevel.HARD, 500, "Get a total of 500 kills");
    public static final Achievement SERIAL_KILLER_III = new Achievement("serial-killer-III", "Serial Killer III", DifficultLevel.HARD, 1000, "Get a total of 1000 kills");
    public static final Achievement SERIAL_KILLER_IV = new Achievement("serial-killer-IV", "Serial Killer IV", DifficultLevel.INSANE, 10000, "Get a total of 10000 kills");
    public static final Achievement SERIAL_KILLER_V = new Achievement("serial-killer-V", "Serial Killer V", DifficultLevel.INSANE, 10000, "Get a total of 10000 kills");

    // Place Blocks & Break Blocks
    public static final Achievement BLOCK_PLACER_I = new Achievement("block-placer-I", "Block Placer I", DifficultLevel.EASY, 500, "Place a total of 500 blocks");
    public static final Achievement BLOCK_PLACER_II = new Achievement("block-placer-II", "Block Placer II", DifficultLevel.NORMAL, 5000, "Place a total of 5000 blocks");
    public static final Achievement BLOCK_PLACER_III = new Achievement("block-placer-III", "Block Placer III", DifficultLevel.NORMAL, 50000, "Place a total of 50 000 blocks");
    public static final Achievement BLOCK_PLACER_IV = new Achievement("block-placer-IV", "Block Placer IV", DifficultLevel.HARD, 500000, "Place a total of 500 000 blocks");
    public static final Achievement BLOCK_PLACER_V = new Achievement("block-placer-V", "Block Placer V", DifficultLevel.INSANE, 1000000, "Place a total of 1 000 000 blocks");

    public static final Achievement GRIEFER_I = new Achievement("griefer-I", "Griefer I", DifficultLevel.EASY, 500, "Destroy a total of 500 blocks");
    public static final Achievement GRIEFER_II = new Achievement("griefer-II", "Griefer II", DifficultLevel.NORMAL, 5000, "Destroy a total of 5000 blocks");
    public static final Achievement GRIEFER_III = new Achievement("griefer-III", "Griefer III", DifficultLevel.NORMAL, 50000, "Destroy a total of 50 000 blocks");
    public static final Achievement GRIEFER_IV = new Achievement("griefer-IV", "Griefer IV", DifficultLevel.HARD, 500000, "Destroy a total of 500 000 blocks");
    public static final Achievement GRIEFER_V = new Achievement("griefer-V", "Griefer V", DifficultLevel.INSANE, 1000000, "Destroy a total of 1 000 000 blocks");

    // Misc
    public static final Achievement GOING_UP = new Achievement("going-up", "Going Up!", DifficultLevel.NORMAL, "Purchase a donator rank and show your support");
    public static final Achievement HELP = new Achievement("help", "Help!", DifficultLevel.EASY, "Search for help with /help");
    public static final Achievement FIRST_FRIEND = new Achievement("first-friend", "Your very first friend", DifficultLevel.EASY, "Add a friend to you friendlist using the /friend command");

    //Login Achievements
    public static final Achievement FIRST_JOIN = new Achievement("first-join", "Welcome!", DifficultLevel.EASY, "Join the server for the first time!");
    public static final Achievement LOGINS_I = new Achievement("logins-I", "Logins I", DifficultLevel.NORMAL, 100, "Login to the server a total of 100 times");
    public static final Achievement LOGINS_II = new Achievement("logins-II", "Logins II", DifficultLevel.HARD, 500, "Login to the server a total of 500 times");
    public static final Achievement LOGINS_III = new Achievement("logins-III", "Logins III", DifficultLevel.HARD, 1000, "Login to the server a total of 1000 times");
    public static final Achievement LOGINS_IV = new Achievement("logins-IV", "Logins IV", DifficultLevel.HARD, 10000, "Login to the server a total of 10 000 times");


    private final List<String> desc; //Description, requirements etc
    private final String display; //What will be displayed in-game
    private final String key; //This is what you should use in the database, as a "key"
    private final DifficultLevel dif; //Difficulty, harder = more points
    private final int req; //Progress thing

    //Non-progressable achievements
    protected Achievement(String key, String displayName, DifficultLevel dif, String... desc) {
        this(key, displayName, dif, -1, desc);
    }

    protected Achievement(String key, String displayName, DifficultLevel dif, int requirement, String... desc) {
        this.key = key;
        this.display = displayName;
        this.dif = dif;
        this.desc = Arrays.asList(desc);
        this.req = requirement;

        registered.add(this);
    }

    public String getKey() {
        return this.key;
    }

    public int getRequirement() {
        return this.req;
    }

    public String getDisplayName() {
        return display;
    }

    public DifficultLevel getDifficulty() {
        return this.dif;
    }

    public List<String> getDescription() {
        return this.desc;
    }

    public static List<Achievement> getAll() {
        return registered;
    }

    public static Achievement get(String key) {
        for (int i = 0; i < registered.size(); i++) {
            Achievement ach = registered.get(i);
            if (ach.getKey().equals(key)) return ach;
        }
        return null;
    }

    public enum DifficultLevel {
        EASY("§a§l", 5),
        NORMAL("§e§l", 15),
        HARD("§c§l", 30),
        INSANE("§4§l", 50);

        private final int worth;
        private final String prefix;

        private DifficultLevel(String prefix, int points) {
            this.worth = points;
            this.prefix = prefix;
        }

        public String getPrefix() {
            return this.prefix;
        }

        public String getDisplay() {
            return this.prefix + this.name();
        }

        public int getPoints() {
            return this.worth;
        }
    }
}