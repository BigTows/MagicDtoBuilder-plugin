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

public class TestMagicMethodCompletionProvider extends BaseTestIntellij {

    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureByFile("libs/DtoBuilder.php");
        myFixture.configureByFile("libs/ExampleDto.php");
    }

    public void testCompletionForMagicMethodDtoBuilderCreate() {
        assertPhpCompletionContains("data/completion/completionForMagicCreateMethod.php",
                "getUrl()", "setUrl()", "hasUrl()");
    }

    public void testCompletionForVariable(){
        assertPhpCompletionContains(
                "data/completion/completionForVariable.php",
                "getUrl()", "setUrl()", "hasUrl()"
        );
    }

    public void testCompletionAfterSetterMethod(){
        assertPhpCompletionContains(
                "data/completion/completionAfterSetterMethod.php",
                "getUrl()", "setUrl()", "hasUrl()"
        );
    }

    public void testCompletionAfterGetterMethod(){
        assertPhpCompletionContains(
                "data/completion/completionAfterGetterMethod.php"
        );
    }

}
