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
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import io.github.bigtows.plugin.magicdtobuilder.inspection.local.fix.AppendDtoClassIntoPhpDocParamTagQuickFix;
import io.github.bigtows.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;
import io.github.bigtows.plugin.magicdtobuilder.utils.PhpTypedElementMagicDtoBuilderUtils;
import org.jetbrains.annotations.NotNull;

public class NotFoundDtoTypeInPhpDocParamTagLocalInspector extends LocalInspectionTool {
    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(final @NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return this.buildPhpElementVisitor(holder);
    }

    private PsiElementVisitor buildPhpElementVisitor(ProblemsHolder holder) {
        return new PhpElementVisitor() {
            @Override
            public void visitPhpFunctionCall(FunctionReference reference) {

                Function function = (Function) reference.resolve();
                if (function == null) {
                    return;
                }
                String methodSignature = MagicDtoBuilderSettings.getInstance(reference.getProject()).getSignatureMagicDtoBuilder();
                for (PsiElement parameter : reference.getParameters()) {
                    if (!(parameter instanceof PhpTypedElement)) {
                        continue;
                    }
                    PhpTypedElement typedElement = (PhpTypedElement) parameter;
                    if (typedElement.getDeclaredType().toString().contains(methodSignature)) {
                        processMagicDtoBuilder(typedElement, function, holder);
                    }

                }
            }
        };
    }

    private void processMagicDtoBuilder(PhpTypedElement element, Function function, ProblemsHolder holder) {
        if (function.getDocComment() == null) {
            return;
        }
        String typeDto = PhpTypedElementMagicDtoBuilderUtils.getDtoNameByPhpTypedElement(element);
        String signatureMagicDtoBuilder = MagicDtoBuilderSettings.getInstance(element.getProject()).getSignatureMagicDtoBuilder();
        for (PhpDocParamTag docParamTag : function.getDocComment().getParamTags()) {
            if (typeDto != null
                    && (!docParamTag.getDeclaredType().toString().contains(signatureMagicDtoBuilder)
                    || !docParamTag.getDeclaredType().toString().contains(typeDto))) {
                holder.registerProblem(
                        docParamTag,
                        "Append type dto",
                        new AppendDtoClassIntoPhpDocParamTagQuickFix(typeDto)
                );
            }
        }
    }

}
