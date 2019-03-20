/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.uniteller.plugin.magicdtobuilder.completion;

import test.uniteller.plugin.magicdtobuilder.BaseTestIntellij;

/**
 * Unit tests for AutoComplete (CompletionProvider)
 *
 * @see com.intellij.codeInsight.completion.CompletionProvider
 */
public final class TestMagicMethodCompletionProvider extends BaseTestIntellij {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureByFile("libs/DtoBuilder.php");
        myFixture.configureByFile("libs/ExampleDto.php");
    }

    /**
     * Test completion for magic method dto builder create.
     */
    public void testCompletionForMagicMethodDtoBuilderCreate() {
        assertPhpCompletionContains("data/completion/completionForMagicCreateMethod.php",
                "getUrl()", "setUrl()", "hasUrl()");
    }

    /**
     * Test completion for variable.
     */
    public void testCompletionForVariable() {
        assertPhpCompletionContains(
                "data/completion/completionForVariable.php",
                "getUrl()", "setUrl()", "hasUrl()"
        );
    }

    /**
     * Test completion after setter method.
     */
    public void testCompletionAfterSetterMethod() {
        assertPhpCompletionContains(
                "data/completion/completionAfterSetterMethod.php",
                "getUrl()", "setUrl()", "hasUrl()"
        );
    }

    /**
     * Test completion after getter method.
     */
    public void testCompletionAfterGetterMethod() {
        assertPhpCompletionContains(
                "data/completion/completionAfterGetterMethod.php"
        );
    }

}
