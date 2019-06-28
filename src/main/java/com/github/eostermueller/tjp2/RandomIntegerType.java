package com.github.eostermueller.tjp2;

import java.util.HashMap;
import java.util.Map;

/**
 * @stolenFrom https://codingexplained.com/coding/java/enum-to-integer-and-integer-to-enum
 * @author erikostermueller
 *
 */
public enum RandomIntegerType {
    STATIC_JAVA_UTIL_RANDOM(1),
    CHEAP_RANDOM(2),
    THREAD_LOCAL_JAVA_UTIL_CONCURRENT_RANDOM(3);

    private int value;
    private static Map map = new HashMap<>();

    private RandomIntegerType(int value) {
        this.value = value;
    }

    static {
        for (RandomIntegerType RandomNumberGenerationType : RandomIntegerType.values()) {
            map.put(RandomNumberGenerationType.value, RandomNumberGenerationType);
        }
    }

    public static RandomIntegerType valueOf(int RandomNumberGenerationType) {
        return (RandomIntegerType) map.get(RandomNumberGenerationType);
    }

    public int getValue() {
        return value;
    }
}
