package ru.uniteller.plugin.magicdtobuilder.utils;

public class StringUtils {

    public static String toUpperFirstChar(String data) {
        if (data.length() < 2) {
            return data.toUpperCase();
        }
        return Character.toUpperCase(data.charAt(0)) + data.substring(1);
    }
}
