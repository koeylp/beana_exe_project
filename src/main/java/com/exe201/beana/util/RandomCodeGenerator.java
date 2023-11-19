package com.exe201.beana.util;

import java.util.Locale;
import java.util.UUID;

public class RandomCodeGenerator {
    public static String generateOrderCode() {
        String prefix = "BEA";
        String randomString = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        return prefix + randomString.toUpperCase(Locale.ROOT);
    }
}
