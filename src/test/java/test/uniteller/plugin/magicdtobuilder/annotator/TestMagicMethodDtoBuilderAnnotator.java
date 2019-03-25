/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.uniteller.plugin.magicdtobuilder.annotator;

import com.intellij.lang.annotation.HighlightSeverity;
import io.github.bigtows.plugin.magicdtobuilder.annotator.MagicMethodDtoBuilderAnnotator;
import test.uniteller.plugin.magicdtobuilder.BaseTestIntellij;
import test.uniteller.plugin.magicdtobuilder.bundle.AssertPhpAnnotatorBundle;

/**
 * The type Test magic method dto builder annotator.
 */
public class TestMagicMethodDtoBuilderAnnotator extends BaseTestIntellij {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureByFile("libs/DtoBuilder.php");
        myFixture.configureByFile("libs/AbstractDto.php");
        myFixture.configureByFile("libs/ExampleDto.php");
        myFixture.configureByFile("libs/ExampleDtoWithPrivateProperty.php");
        warmUpPhpIndex();
    }


    /**
     * Test annotator set magic method empty after magic dto builder create.
     */
    public void testAnnotatorSetMagicMethodEmptyAfterMagicDtoBuilderCreate() {
        assertPhpAnnotator(new MagicMethodDtoBuilderAnnotator(),
                AssertPhpAnnotatorBundle.builder()
                        .message("Параметры забыл...")
                        .startOffset(130)
                        .endOffset(132)
                        .severity(HighlightSeverity.ERROR)
                        .build(), "data/annotator/testSetMagicMethodEmptyAfterMagicDtoBuilderCreate.php");
    }

    /**
     * Test annotator set magic method empty after variable.
     */
    public void testAnnotatorSetMagicMethodEmptyAfterVariable() {
        assertPhpAnnotator(new MagicMethodDtoBuilderAnnotator(),
                AssertPhpAnnotatorBundle.builder()
                        .message("Параметры забыл...")
                        .startOffset(151)
                        .endOffset(153)
                        .severity(HighlightSeverity.ERROR)
                        .build(), "data/annotator/testSetMagicMethodEmptyAfterVariable.php");
    }


    /**
     * Test annotation ready remove member has protected access.
     */
    public void testAnnotationReadyRemoveMemberHasProtectedAccess() {
        assertPhpAnnotator(null,
                AssertPhpAnnotatorBundle.builder()
                        .message("Member has protected access")
                        .startOffset(145)
                        .endOffset(151)
                        .severity(HighlightSeverity.ERROR)
                        .build(), "data/annotator/testAnnotationReadyRemoveMemberHasProtectedAccess.php");

        assertNotExistsPhpAnnotator(new MagicMethodDtoBuilderAnnotator(),
                AssertPhpAnnotatorBundle.builder()
                        .message("Member has protected access")
                        .startOffset(145)
                        .endOffset(151)
                        .severity(HighlightSeverity.ERROR)
                        .build(), "data/annotator/testAnnotationReadyRemoveMemberHasProtectedAccess.php");

    }

    /**
     * Test annotation ready remove member has private access.
     */
    public void testAnnotationReadyRemoveMemberHasPrivateAccess() {
        assertPhpAnnotator(null,
                AssertPhpAnnotatorBundle.builder()
                        .message("Member has private access")
                        .startOffset(183)
                        .endOffset(189)
                        .severity(HighlightSeverity.ERROR)
                        .build(), "data/annotator/testAnnotationReadyRemoveMemberHasPrivateAccess.php");

        assertNotExistsPhpAnnotator(new MagicMethodDtoBuilderAnnotator(),
                AssertPhpAnnotatorBundle.builder()
                        .message("Member has private access")
                        .startOffset(183)
                        .endOffset(189)
                        .severity(HighlightSeverity.ERROR)
                        .build(), "data/annotator/testAnnotationReadyRemoveMemberHasPrivateAccess.php");

    }
}
