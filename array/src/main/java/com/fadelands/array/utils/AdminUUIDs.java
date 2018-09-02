package com.fadelands.array.utils;

import java.util.*;

public class AdminUUIDs {

    public static List<UUID> uuids = new ArrayList<>();

    public AdminUUIDs() {
        //admins
        AdminUUIDs.uuids.add(UUID.fromString("0678f266-8845-4140-bc82-0e4c18b61af1")); // arrayofc
        AdminUUIDs.uuids.add(UUID.fromString("d8bbc199-1e1a-4192-8d41-bb05fd2210c7")); // spacetrain31
        AdminUUIDs.uuids.add(UUID.fromString("4b2dc909-cdad-4530-a03b-612bcabbcecd")); // Robskiy
        AdminUUIDs.uuids.add(UUID.fromString("a2422e58-5c5d-4751-a45d-802e51ebba55")); // hataglass (xenyor)

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
