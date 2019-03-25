/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.uniteller.plugin.magicdtobuilder.providers.type;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import org.junit.Assert;
import test.uniteller.plugin.magicdtobuilder.BaseTestIntellij;

import java.util.ArrayList;
import java.util.Set;

/**
 * The type Test dto builder type provider.
 */
public class TestDtoBuilderTypeProvider extends BaseTestIntellij {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureByFile("libs/DtoBuilder.php");
        myFixture.configureByFile("libs/AbstractDto.php");
        myFixture.configureByFile("libs/ExampleDto.php");
        warmUpPhpIndex();
    }


    /**
     * Test type provider has method at create method magic dto builder.
     */
    public void testTypeProviderHasMethodAtCreateMethodMagicDtoBuilder() {
        myFixture.configureByFile("data/providers/type/typeProviderHasMethodAtCreateMethodMagicDtoBuilder.php");
        PsiElement elementAtCaret = this.getPsiElementAtCaret();
        if (!(elementAtCaret.getParent() instanceof MethodReference)) {
            fail("For test: testTypeProviderHasMethodAtCreateMethodMagicDtoBuilder, caret need set at method reference");
        }
        Set<String> stringSet = ((MethodReference) elementAtCaret.getParent()).getDeclaredType().getTypesSorted();


        Assert.assertArrayEquals(new String[]{
                "#M#C\\App\\Library\\ExampleApi\\ExampleDto.hasUrl",
                "#M#M#C\\App\\Library\\DtoBuilder\\DtoBuilder.create.hasUrl",
                "\\bool"
        }, stringSet.toArray());
    }

    /**
     * Test type provider setter method at create method magic dto builder.
     */
    public void testTypeProviderSetterMethodAtCreateMethodMagicDtoBuilder() {
        myFixture.configureByFile("data/providers/type/typeProviderSetterMethodAtCreateMethodMagicDtoBuilder.php");
        PsiElement elementAtCaret = this.getPsiElementAtCaret();
        if (!(elementAtCaret.getParent() instanceof MethodReference)) {
            fail("For test: typeProviderSetterMethodAtCreateMethodMagicDtoBuilder, caret need set at method reference");
        }
        Set<String> stringSet = ((MethodReference) elementAtCaret.getParent()).getDeclaredType().getTypesSorted();


        Assert.assertArrayEquals(new String[]{
                "#M#C\\App\\Library\\ExampleApi\\ExampleDto.setUrl",
                "#M#M#C\\App\\Library\\DtoBuilder\\DtoBuilder.create.setUrl",
                "\\App\\Library\\DtoBuilder\\DtoBuilder",
                "\\App\\Library\\ExampleApi\\ExampleDto"
        }, stringSet.toArray());
    }


    /**
     * Test type provider setter method at create method magic dto builder.
     */
    public void testTypeProviderGetterMethodAtCreateMethodMagicDtoBuilder() {
        myFixture.configureByFile("data/providers/type/typeProviderGetterMethodAtCreateMethodMagicDtoBuilder.php");
        PsiElement elementAtCaret = this.getPsiElementAtCaret();
        if (!(elementAtCaret.getParent() instanceof MethodReference)) {
            fail("For test: typeProviderGetterMethodAtCreateMethodMagicDtoBuilder, caret need set at method reference");
        }
        Set<String> stringSet = ((MethodReference) elementAtCaret.getParent()).getDeclaredType().getTypesSorted();


        Assert.assertArrayEquals(new String[]{
                "#M#M#C\\App\\Library\\DtoBuilder\\DtoBuilder.create.getUrl",
                "#M#☘\\App\\Library\\ExampleApi\\ExampleDto.getUrl",
                "\\string"
        }, stringSet.toArray());
    }
}
