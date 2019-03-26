/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package io.github.bigtows.plugin.magicdtobuilder.utils;

/**
 * Utils method for String class
 */
public final class StringUtils {

    /**
     * Upper first char in text
     *
     * @param data any text
     * @return prepared text
     */
    public static String toUpperFirstChar(String data) {
        if (data.length() < 2) {
            return data.toUpperCase();
        }
        return Character.toUpperCase(data.charAt(0)) + data.substring(1);
    }


    /**
     * Lower first char in text
     *
     * @param data any text
     * @return prepared text
     */
    public static String toLowerFirstChar(String data) {
        if (data.length() < 2) {
            return data.toLowerCase();
        }
        return Character.toLowerCase(data.charAt(0)) + data.substring(1);
    }
}
