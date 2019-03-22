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

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.QuickFix;
import com.intellij.openapi.command.WriteCommandAction;
import io.github.bigtows.plugin.magicdtobuilder.inspection.local.fix.AppendDtoClassIntoPhpDocParamTagQuickFix;
import org.junit.Assert;
import test.uniteller.plugin.magicdtobuilder.BaseTestIntellij;
import test.uniteller.plugin.magicdtobuilder.bundle.AssertPhpLocalInspectionBundle;

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
                AssertPhpLocalInspectionBundle.build("Append type dto", 93)
                        .addQuickFixClasses(AppendDtoClassIntoPhpDocParamTagQuickFix.class)
        );

        ProblemDescriptor problemDescriptor = getPhpLocalInspectionContains("data/inspection/UpdatePhpDocParamTag.php").get(0);
        if (problemDescriptor.getFixes() == null || problemDescriptor.getFixes().length != 1) {
            fail("Can't find fixes");
            return;
        }
        LocalQuickFix quickFix = (LocalQuickFix) problemDescriptor.getFixes()[0];
        WriteCommandAction.runWriteCommandAction(myFixture.getProject(), () -> {
            quickFix.applyFix(myFixture.getProject(), problemDescriptor);
            String resultText = getPsiElementAtCaret().getParent().getParent().getParent().getText();
            Assert.assertEquals(getTextFromFile("data/inspection/UpdatePhpDocParamTagAfterQuickFix.php"), resultText);
        });
    }

    /**
     * Test update php doc param tag if function reference storage in function.
     */
    public void testUpdatePhpDocParamTagIfFunctionReferenceStorageInFunction() {
        assertPhpLocalInspectionContains(
                "data/inspection/UpdatePhpDocParamTagIfFunctionReferenceStorageInFunction.php",
                AssertPhpLocalInspectionBundle.build("Append type dto", 144)
                        .addQuickFixClasses(AppendDtoClassIntoPhpDocParamTagQuickFix.class)
        );
    }
}
