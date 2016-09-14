package com.nihao001.sso.common.utils;

public class Assert {

    public static void notBlank(String str, String message) {
        if (Utils.isStringBlank(str)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, String message) {

        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertTrue(boolean bool, String message) {
        if (!bool) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertTrue(boolean bool) {
        assertTrue(bool, "");
    }

    public static void assertFalse(boolean bool, String message) {
        if (bool) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertFalse(boolean bool) {
        assertFalse(bool, "");
    }

}
