package org.example.filemapper.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class HashUtil {
    public static long hash64(String key) {
        byte[] bytes = Hashing.murmur3_128()
                .hashString(key, StandardCharsets.UTF_8)
                .asBytes();
        long val = 0;
        for (int i = 0; i < 8; i++) {
            val = (val << 8) | (bytes[i] & 0xffL);
        }
        long unsigned = val >= 0 ? val : val + (1L << 64);
        return val;
    }
}
