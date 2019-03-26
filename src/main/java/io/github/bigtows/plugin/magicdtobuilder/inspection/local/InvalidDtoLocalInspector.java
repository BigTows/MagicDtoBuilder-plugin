/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package io.github.bigtows.plugin.magicdtobuilder.inspection.local;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.ClassConstantReference;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import io.github.bigtows.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;
import io.github.bigtows.plugin.magicdtobuilder.utils.MagicMethodDtoBuilderUtils;
import io.github.bigtows.plugin.magicdtobuilder.utils.PhpClassUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class InvalidDtoLocalInspector extends LocalInspectionTool {
    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(final @NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return this.buildPhpElementVisitor(holder);
    }

    /**
     * Build php element visitor
     *
     * @param holder problems holder
     * @return php element visitor
     */
    private PhpElementVisitor buildPhpElementVisitor(ProblemsHolder holder) {
        return new PhpElementVisitor() {
            @Override
            public void visitPhpMethodReference(MethodReference reference) {
                if (MagicMethodDtoBuilderUtils.isCreateMethodDtoBuilder(reference)) {
                    processCreateMethodDtoBuilder(reference, holder);
                }
            }
        };
    }

    /**
     * Process create method dto builder
     *
     * @param methodReference method reference
     * @param holder          holder
     */
    private void processCreateMethodDtoBuilder(MethodReference methodReference, ProblemsHolder holder) {
        if (methodReference.getParameterList() == null || methodReference.getParameters().length != 1) {
            return;
        }
        PsiElement psiElement = methodReference.getParameters()[0];
        if (psiElement instanceof ClassConstantReference) {
            PhpExpression phpExpression = ((ClassConstantReference) psiElement).getClassReference();
            if (phpExpression != null) {
                String FQN = phpExpression.getDeclaredType().toString();
                Collection<PhpClass> phpClassCollection = PhpIndex.getInstance(methodReference.getProject()).getClassesByFQN(FQN);
                String fqnAbstractDto = MagicDtoBuilderSettings.getInstance(methodReference.getProject()).getSignatureAbstractDto();
                if (phpClassCollection.stream().noneMatch(PhpClassUtils::isPhpClassExtendedAbstractDto)) {
                    holder.registerProblem(methodReference.getParameterList(), "DTO must extended at " + fqnAbstractDto, ProblemHighlightType.ERROR);
                }
            }
        }
    }
}
