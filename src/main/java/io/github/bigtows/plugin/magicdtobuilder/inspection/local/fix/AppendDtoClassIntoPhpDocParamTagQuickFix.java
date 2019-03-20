/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package io.github.bigtows.plugin.magicdtobuilder.inspection.local.fix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class AppendDtoClassIntoPhpDocParamTagQuickFix implements LocalQuickFix {

    private final String typeOfDto;

    public AppendDtoClassIntoPhpDocParamTagQuickFix(String typeOfDto) {
        this.typeOfDto = typeOfDto;
    }


    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "Update PHPDoc Comment";
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        if (!(descriptor.getPsiElement() instanceof PhpDocParamTag)) {
            return;
        }
        PhpDocParamTag phpDocParamTag = (PhpDocParamTag) descriptor.getPsiElement();
        String phpDocText = phpDocParamTag.getParent().getText();
        phpDocText = phpDocText.replace(phpDocParamTag.getText(), "@param " + phpDocParamTag.getDeclaredType().toString() + "|" + typeOfDto + " $" + phpDocParamTag.getVarName());
        PsiElement psiElement = PhpPsiElementFactory.createFromText(phpDocParamTag.getProject(), PhpDocComment.class, phpDocText);
        if (psiElement == null) {
            //Log error.
            return;
        }
        phpDocParamTag.getParent().replace(psiElement);
    }
}
