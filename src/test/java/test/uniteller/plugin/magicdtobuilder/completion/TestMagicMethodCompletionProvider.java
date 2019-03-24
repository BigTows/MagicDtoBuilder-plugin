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
        myFixture.configureByFile("libs/AbstractDto.php");
        myFixture.configureByFile("libs/ExampleDto.php");
        myFixture.configureByFile("libs/ExampleDtoWithTrait.php");
        myFixture.configureByFile("libs/TraitDto.php");
    }

    /**
     * Test completion for magic method dto builder create.
     */
    public void testCompletionForMagicMethodDtoBuilderCreate() {
        assertPhpCompletionContains("data/completion/completionForMagicCreateMethod.php",
                "getUrl()", "setUrl()", "hasUrl()", "build"
        );
    }

    /**
     * Test completion for variable.
     */
    public void testCompletionForVariable() {
        assertPhpCompletionContains(
                "data/completion/completionForVariable.php",
                "getUrl()", "setUrl()", "hasUrl()", "build"
        );
    }

    /**
     * Test completion after setter method.
     */
    public void testCompletionAfterSetterMethod() {
        assertPhpCompletionContains(
                "data/completion/completionAfterSetterMethod.php",
                "getUrl()", "setUrl()", "hasUrl()", "build"
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


    /**
     * Test completion after setter method and variable.
     */
    public void testCompletionAfterSetterMethodAndVariable() {
        assertPhpCompletionContains(
                "data/completion/completionAfterSetterMethodAndVariable.php",
                "getUrl()", "setUrl()", "hasUrl()", "build"
        );
    }

    /**
     * Test completion for variable with php doc.
     */
    public void testCompletionForVariableWithPhpDoc() {
        assertPhpCompletionContains(
                "data/completion/completionForVariableWithPhpDoc.php",
                "getUrl()", "setUrl()", "hasUrl()", "build"
        );
    }

    /**
     * Test completion for variable with invalid php doc.
     */
    public void testCompletionForVariableWithInvalidPhpDoc() {
        assertPhpCompletionContains(
                "data/completion/completionForVariableWithInvalidPhpDoc.php",
                "build"
        );
    }

    /**
     * Test completion for dto with trait.
     */
    public void testCompletionForDtoWithTrait() {
        assertPhpCompletionContains(
                "data/completion/completionForDtoWithTrait.php",
                "getDataTrait()", "getUrl()", "hasDataTrait()", "hasUrl()", "setDataTrait()", "setUrl()", "build"
        );
    }

}
