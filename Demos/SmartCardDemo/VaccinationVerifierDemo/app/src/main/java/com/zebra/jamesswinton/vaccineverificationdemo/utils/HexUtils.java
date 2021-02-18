package com.zebra.jamesswinton.vaccineverificationdemo.utils;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class HexUtils {

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static byte[] trim(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) { --i; }
        return Arrays.copyOf(bytes, i + 1);
    }

    @NonNull
    public static String byteArrayToHexString(byte[] src) {
        // Validate Input
        if (src == null || src.length <= 0) {
            return "";
        }

        // Init String Builder & Buffer
        char[] buffer = new char[2];
        StringBuilder stringBuilder = new StringBuilder("0x");

        // Convert each Byte to Hex
        for (byte b : src) {
            buffer[0] = Character.forDigit((b >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(b & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        // Return String
        return stringBuilder.toString();
    }

}
