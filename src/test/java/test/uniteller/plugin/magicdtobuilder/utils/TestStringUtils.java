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

public class TestStringUtils extends BaseTestIntellij {

    public void testStringUtils() {
        Assert.assertEquals("A", StringUtils.toUpperFirstChar("a"));
        Assert.assertEquals("BA", StringUtils.toUpperFirstChar("bA"));
        Assert.assertEquals("Url", StringUtils.toUpperFirstChar("url"));
    }
}
