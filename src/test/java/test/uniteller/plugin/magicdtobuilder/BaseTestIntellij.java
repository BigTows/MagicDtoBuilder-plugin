/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.uniteller.plugin.magicdtobuilder;

import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.codeInspection.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;

import java.util.List;


public abstract class BaseTestIntellij extends LightCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources";
    }

    /**
     * PHP Completion assert
     *
     * @param filePath      file to data.
     * @param lookupStrings excepted data
     */
    protected void assertPhpCompletionContains(String filePath, String... lookupStrings) {
        myFixture.configureByFile(filePath);
        myFixture.completeBasic();
        completionContainsAssert(lookupStrings, myFixture.getLookupElementStrings());
    }

    /**
     * Completion assert between myFixture and Excepted data
     *
     * @param exceptedLookupElementStrings excepted data
     * @param lookupElementStrings         myFixture lookup elements
     */
    private void completionContainsAssert(String[] exceptedLookupElementStrings, List<String> lookupElementStrings) {
        List<String> lookupElements = myFixture.getLookupElementStrings();
        if (lookupElements == null) {
            fail("Error completionContainsAssert. (myFixture.getLookupElementStrings return NULL)");
        }
        if (exceptedLookupElementStrings.length == 0 && lookupElements.size() != 0) {
            fail(String.format(
                    "Lookup elements constrains unnecessary data. Available: %s",
                    String.join(", ", lookupElements))
            );
        }
        for (String lookupString : exceptedLookupElementStrings) {
            if (!lookupElements.contains(lookupString)) {
                fail(
                        String.format("Can't found %s in lookup elements. Available: %s",
                                lookupString,
                                String.join(", ", lookupElements)
                        )
                );
            }
        }
    }

    /**
     * Assert php local inspection
     *
     * @param filePath file path
     * @param data     data for assert
     */
    protected void assertPhpLocalInspectionContains(@NotNull String filePath, @Nullable AssertPhpLocalInspectionData data) {
        List<ProblemDescriptor> problemDescriptorList = getPhpProblemsDescriptor(filePath);

        int countFounded = 0;
        if (data != null) {
            for (ProblemDescriptor problemDescriptor : problemDescriptorList) {
                if (data.getTemplateDescription().equals(problemDescriptor.getDescriptionTemplate())) {
                    countFounded++;
                    Assert.assertEquals("Check offset text", data.getOffsetTextProblem(), problemDescriptor.getPsiElement().getTextOffset());
                    Assert.assertEquals("Check quickFix count", data.getStorageQuickFixClasses().size(), problemDescriptor.getFixes().length);
                    for (int i = 0; i < problemDescriptor.getFixes().length; i++) {
                        Assert.assertEquals("Check quickFix class", data.getStorageQuickFixClasses().get(i).toString(), problemDescriptor.getFixes()[i].getClass().toString());
                    }
                }
            }
        }

        if (countFounded == 0 && data != null) {
            fail(
                    String.format("Can't found problem descriptor with template description %s", data.getTemplateDescription())
            );
        }
        if (data == null && problemDescriptorList.size() > 0) {
            fail("Founded extra problem descriptor");
        }
    }

    /**
     * Get problems descriptor in file
     *
     * @param filePath file path
     * @return list of problem descriptor
     */
    private List<ProblemDescriptor> getPhpProblemsDescriptor(String filePath) {

        PsiElement psiFile = myFixture.configureByFile(filePath);

        int caretOffset = myFixture.getCaretOffset();
        if (caretOffset <= 0) {
            fail("<caret> tag not initialized");
        }
        ProblemsHolder problemsHolder = new ProblemsHolder(InspectionManager.getInstance(getProject()), psiFile.getContainingFile(), false);

        for (LocalInspectionEP localInspectionEP : LocalInspectionEP.LOCAL_INSPECTION.getExtensions()) {
            Object object = localInspectionEP.getInstance();
            if (!(object instanceof LocalInspectionTool)) {
                continue;
            }

            final PsiElementVisitor psiElementVisitor = ((LocalInspectionTool) object).buildVisitor(problemsHolder, false);

            if (psiElementVisitor instanceof PhpElementVisitor) {
                psiFile.acceptChildren(new PhpElementVisitor() {
                    @Override
                    public void visitElement(PsiElement element) {
                        PsiElement psiElementAtCaret = psiFile.findElementAt(caretOffset);
                        if (psiElementAtCaret == null) {
                            return;
                        }
                        if (psiElementAtCaret.getParent() instanceof FunctionReference) {
                            ((PhpElementVisitor) psiElementVisitor).visitPhpFunctionCall((FunctionReference) psiElementAtCaret.getParent());
                        }
                        super.visitElement(element);
                    }
                });
                ((PhpElementVisitor) psiElementVisitor).visitPhpFile((PhpFile) psiFile.getContainingFile());
            }
        }

        return problemsHolder.getResults();
    }

    /**
     * Warm up php index.
     */
    protected void warmUpPhpIndex() {
        PhpIndex phpIndex = PhpIndex.getInstance(getProject());
        for (String FQN : phpIndex.getAllClassFqns(PrefixMatcher.ALWAYS_TRUE)) {
            phpIndex.getClassesByFQN(FQN);
        }
    }
}
