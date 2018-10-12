package com.fadelands.core.utils;

import java.util.UUID;

public class StringUtils {
    public static UUID uuidFromStringWithoutDashes(String digits) {
        String uuid = digits.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
        return UUID.fromString(uuid);
    }
}
