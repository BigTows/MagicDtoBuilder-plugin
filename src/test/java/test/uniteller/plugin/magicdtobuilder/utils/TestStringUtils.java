/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.uniteller.plugin.magicdtobuilder.utils;

import io.github.bigtows.plugin.magicdtobuilder.utils.StringUtils;
import org.junit.Assert;
import test.uniteller.plugin.magicdtobuilder.BaseTestIntellij;

/**
 * The type Test string utils.
 */
public class TestStringUtils extends BaseTestIntellij {

    /**
     * Test string utils.
     */
    public void testStringUtils() {
        Assert.assertEquals("A", StringUtils.toUpperFirstChar("a"));
        Assert.assertEquals("BA", StringUtils.toUpperFirstChar("bA"));
        Assert.assertEquals("Url", StringUtils.toUpperFirstChar("url"));

        Assert.assertEquals("a", StringUtils.toLowerFirstChar("A"));
        Assert.assertEquals("bA", StringUtils.toLowerFirstChar("BA"));
        Assert.assertEquals("url", StringUtils.toLowerFirstChar("Url"));
    }
}
