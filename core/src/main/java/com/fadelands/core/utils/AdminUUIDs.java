package com.fadelands.core.utils;

import java.util.*;

public class AdminUUIDs {

    public static List<UUID> uuids = new ArrayList<>();

    public AdminUUIDs() {
        //admins
        AdminUUIDs.uuids.add(UUID.fromString("0678f266-8845-4140-bc82-0e4c18b61af1")); // arrayofc
        AdminUUIDs.uuids.add(UUID.fromString("06f14eb1-8f8f-45cb-9e13-eb9d65f16650")); // Strqfe_
        AdminUUIDs.uuids.add(UUID.fromString("d8bbc199-1e1a-4192-8d41-bb05fd2210c7")); // spacetrain31
        AdminUUIDs.uuids.add(UUID.fromString("4b2dc909-cdad-4530-a03b-612bcabbcecd")); // Robskiy
        AdminUUIDs.uuids.add(UUID.fromString("20d09221-cee7-42a7-9a3b-26dd87849cad")); // xenyor

        //devs
        AdminUUIDs.uuids.add(UUID.fromString("7cd493a1-1214-4da3-9ac1-a0bfef50b75c")); // stevyb0t
        AdminUUIDs.uuids.add(UUID.fromString("c4e28ac8-922b-4db3-98c6-d875b5701754")); // Purox

    }

    public static boolean check(UUID uuid) {
        for (UUID id : uuids) {
            if (uuid.equals(id)) return true;
        }
        return false;
    }
}
