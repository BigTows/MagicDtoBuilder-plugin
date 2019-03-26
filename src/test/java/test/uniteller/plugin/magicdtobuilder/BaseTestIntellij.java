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
import com.intellij.codeInsight.daemon.impl.AnnotationHolderImpl;
import com.intellij.codeInspection.*;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageAnnotators;
import com.intellij.lang.annotation.AnnotationSession;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.MemberReference;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import test.uniteller.plugin.magicdtobuilder.bundle.AssertPhpAnnotatorBundle;
import test.uniteller.plugin.magicdtobuilder.bundle.AssertPhpLocalInspectionBundle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;


/**
 * The type Base test intellij.
 */
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
     * @param lookupElements               myFixture lookup elements
     */
    private void completionContainsAssert(String[] exceptedLookupElementStrings, List<String> lookupElements) {
        if (lookupElements == null) {
            fail("Error completionContainsAssert. (myFixture.getLookupElementStrings return NULL)");
        }
        if (exceptedLookupElementStrings.length != lookupElements.size()) {
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
    protected void assertPhpLocalInspectionContains(@NotNull String filePath, @Nullable AssertPhpLocalInspectionBundle data) {
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
     * Get php local inspection contains list.
     *
     * @param filePath the file path
     * @return the list
     */
    protected List<ProblemDescriptor> getPhpLocalInspectionContains(@NotNull String filePath) {
        return getPhpProblemsDescriptor(filePath);
    }

    /**
     * Get problems descriptor in file
     *
     * @param filePath file path
     * @return list of problem descriptor
     */
    private List<ProblemDescriptor> getPhpProblemsDescriptor(String filePath) {

        PsiElement psiFile = myFixture.configureByFile(filePath);
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
                        PsiElement psiElementAtCaret = getPsiElementAtCaret();
                        if (psiElementAtCaret == null) {
                            return;
                        }
                        if (psiElementAtCaret.getParent() instanceof MemberReference) {
                            ((PhpElementVisitor) psiElementVisitor).visitPhpMethodReference((MethodReference) psiElementAtCaret.getParent());
                        } else if (psiElementAtCaret.getParent() instanceof FunctionReference) {
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
     * Get psi element at caret
     *
     * @return psi element at caret
     */
    protected PsiElement getPsiElementAtCaret() {
        PsiElement psiFile = myFixture.getFile();
        int caretOffset = myFixture.getCaretOffset();
        if (caretOffset <= 0) {
            fail("Can't find <caret>, you may have forgotten to load the file?");
        }
        return psiFile.findElementAt(caretOffset);
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


    /**
     * Assert php annotator.
     *
     * @param annotator                the annotator
     * @param assertPhpAnnotatorBundle the assert php annotator
     * @param phpFilePath              the php file path
     */
    protected void assertPhpAnnotator(@Nullable Annotator annotator, AssertPhpAnnotatorBundle assertPhpAnnotatorBundle, @NotNull String phpFilePath) {
        AnnotationHolderImpl annotationHolder = this.executeAnnotatorsForFile(annotator, phpFilePath);
        if (annotationHolder.stream().noneMatch(annotation ->
                annotation.getStartOffset() == assertPhpAnnotatorBundle.getStartOffset()
                        && annotation.getEndOffset() == assertPhpAnnotatorBundle.getEndOffset()
                        && annotation.getMessage().equals(assertPhpAnnotatorBundle.getMessage())
                        && annotation.getSeverity().equals(assertPhpAnnotatorBundle.getSeverity()))) {
            fail(String.format("Can't find annotation with property: %s", assertPhpAnnotatorBundle.toString()));
        }
    }

    /**
     * Assert php annotator.
     *
     * @param annotator                the annotator
     * @param assertPhpAnnotatorBundle the assert php annotator
     * @param phpFilePath              the php file path
     */
    protected void assertNotExistsPhpAnnotator(@Nullable Annotator annotator, AssertPhpAnnotatorBundle assertPhpAnnotatorBundle, @NotNull String phpFilePath) {
        AnnotationHolderImpl annotationHolder = this.executeAnnotatorsForFile(annotator, phpFilePath);
        if (annotationHolder.stream().anyMatch(annotation ->
                annotation.getStartOffset() == assertPhpAnnotatorBundle.getStartOffset()
                        && annotation.getEndOffset() == assertPhpAnnotatorBundle.getEndOffset()
                        && annotation.getMessage().equals(assertPhpAnnotatorBundle.getMessage())
                        && annotation.getSeverity().equals(assertPhpAnnotatorBundle.getSeverity()))) {
            fail(String.format("Find annotation with property: %s", assertPhpAnnotatorBundle.toString()));
        }
    }


    /**
     * Execute annotator for php file with path
     *
     * @param ownerAnnotator owner annotator
     * @param phpFilePath    php file path
     * @return annotation holder
     */
    private AnnotationHolderImpl executeAnnotatorsForFile(@Nullable Annotator ownerAnnotator, String phpFilePath) {
        PsiFile psiFile = myFixture.configureByFile(phpFilePath);

        AnnotationHolderImpl annotationHolder = new AnnotationHolderImpl(new AnnotationSession(psiFile));
        //Before class default annotator
        this.callAnnotatorForPsiFile(LanguageAnnotators.INSTANCE.forLanguage(Objects.requireNonNull(Language.findLanguageByID("PHP"))), annotationHolder, psiFile);
        //Call user annotator
        this.callAnnotatorForPsiFile(ownerAnnotator, annotationHolder, psiFile);
        return annotationHolder;
    }

    /**
     * Process psi file with annotator
     *
     * @param annotator instance of annotator
     * @param context   Annotation holder
     * @param psiFile   psi file
     */
    private void callAnnotatorForPsiFile(@Nullable Annotator annotator, AnnotationHolderImpl context, PsiFile psiFile) {
        if (annotator == null) {
            return;
        }
        psiFile.accept(new PsiRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                annotator.annotate(element, context);
                super.visitElement(element);
            }
        });
    }


    protected String getTextFromFile(@NotNull String filePath) {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            return new String(Files.readAllBytes(new File(classLoader.getResource(filePath).getFile()).toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
