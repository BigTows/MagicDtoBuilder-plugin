/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.uniteller.plugin.magicdtobuilder.settings;

import io.github.bigtows.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;
import org.junit.Assert;
import test.uniteller.plugin.magicdtobuilder.BaseTestIntellij;

/**
 * The type Test magic dto builder settings.
 */
public class TestMagicDtoBuilderSettings extends BaseTestIntellij {

    private MagicDtoBuilderSettings settings;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        settings = MagicDtoBuilderSettings.getInstance(myFixture.getProject());
    }

    /**
     * Test.
     */
    public void test() {
        Assert.assertEquals(settings, settings.getState());
        settings.loadState(settings);
    }
}
