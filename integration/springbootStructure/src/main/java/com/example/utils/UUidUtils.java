package com.example.utils;

import java.util.UUID;

public class UUidUtils {

    public static String getId() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
