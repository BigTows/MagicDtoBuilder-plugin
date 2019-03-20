/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.uniteller.plugin.magicdtobuilder.inspection;

import io.github.bigtows.plugin.magicdtobuilder.inspection.local.fix.AppendDtoClassIntoPhpDocParamTagQuickFix;
import test.uniteller.plugin.magicdtobuilder.AssertPhpLocalInspectionData;
import test.uniteller.plugin.magicdtobuilder.BaseTestIntellij;

/**
 * Unit tests for local inspection
 *
 * @see com.intellij.codeInspection.LocalInspectionTool
 */
public class TestLocalInspection extends BaseTestIntellij {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureByFile("libs/DtoBuilder.php");
        myFixture.configureByFile("libs/ExampleDto.php");
        warmUpPhpIndex();
    }

    /**
     * Test update php doc param tag.
     */
    public void testUpdatePhpDocParamTag() {
        assertPhpLocalInspectionContains(
                "data/inspection/UpdatePhpDocParamTag.php",
                AssertPhpLocalInspectionData.build("Append type dto", 93)
                        .addQuickFixClasses(AppendDtoClassIntoPhpDocParamTagQuickFix.class)
        );
    }

    /**
     * Test update php doc param tag if function reference storage in function.
     */
    public void testUpdatePhpDocParamTagIfFunctionReferenceStorageInFunction() {
        assertPhpLocalInspectionContains(
                "data/inspection/UpdatePhpDocParamTagIfFunctionReferenceStorageInFunction.php",
                AssertPhpLocalInspectionData.build("Append type dto", 144)
                        .addQuickFixClasses(AppendDtoClassIntoPhpDocParamTagQuickFix.class)
        );
    }
}
