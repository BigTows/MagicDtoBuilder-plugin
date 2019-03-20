/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.uniteller.plugin.magicdtobuilder.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.uniteller.plugin.magicdtobuilder.utils.MagicMethodDtoBuilderUtils;
import ru.uniteller.plugin.magicdtobuilder.utils.MethodReferenceUtils;
import ru.uniteller.plugin.magicdtobuilder.utils.PhpTypedElementMagicDtoBuilderUtils;
import ru.uniteller.plugin.magicdtobuilder.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Completion provider magic method.
 */
public class MagicMethodCompletionProvider extends CompletionProvider<CompletionParameters> {

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        if (parameters.getPosition().getPrevSibling() == null) {
            return;
        }
        PsiElement psiElement = parameters.getPosition().getPrevSibling();
        PhpClass phpClass = getDtoClassByPsiElement(psiElement.getPrevSibling());
        if (phpClass != null) {
            phpClass.getFields().forEach(field -> result.addAllElements(this.createMethodLookupElementByField(field)));
        }
    }

    /**
     * Get PHPClass DTO by psi element (MethodReference, Variable)
     *
     * @param element psi element
     * @return if success PHPClass else {@code null}
     */
    @Nullable
    private PhpClass getDtoClassByPsiElement(@Nullable PsiElement element) {
        String FQN = null;
        if (element instanceof MethodReference) {
            FQN = this.getFQNByMethodReference((MethodReference) element);
        } else if (element instanceof Variable) {
            FQN = PhpTypedElementMagicDtoBuilderUtils.getDtoNameByPhpTypedElement((PhpTypedElement) element);
        }
        if (FQN != null) {
            final PhpIndex phpIndex = PhpIndex.getInstance(element.getProject());
            //Return only 1 phpclass, mb need return many...
            return phpIndex.getClassesByFQN(FQN).stream().findFirst().orElse(null);
        }
        return null;
    }

    /**
     * Get FQN class DTO by method reference
     *
     * @param methodReference method reference
     * @return if success FQN class DTO else {@code null}
     */
    @Nullable
    private String getFQNByMethodReference(MethodReference methodReference) {
        if (MagicMethodDtoBuilderUtils.isMagicSetterMethodDtoBuilder(methodReference)) {
            MethodReference root = MethodReferenceUtils.getFirstMethodReference(methodReference);
            if (MagicMethodDtoBuilderUtils.isCreateMethodDtoBuilder(root)) {
                return this.getFQNByMethodReference(root);
            }else{
                return PhpTypedElementMagicDtoBuilderUtils.getDtoNameByPhpTypedElement(methodReference);
            }
        } else if (MagicMethodDtoBuilderUtils.isCreateMethodDtoBuilder(methodReference)) {
            ClassConstantReference classConstantReference = (ClassConstantReference) (methodReference).getParameters()[0];
            if (classConstantReference.getClassReference() == null) {
                return null;
            }
            return classConstantReference.getClassReference().getDeclaredType().toString();
        }
        return null;
    }

    /**
     * Create lookup elements by field
     *
     * @param field field on the basis of which creating lookup elements
     * @return lookup elements
     */
    private List<LookupElement> createMethodLookupElementByField(@NotNull Field field) {
        List<LookupElement> lookupElements = new ArrayList<>();
        String preparedNameField = StringUtils.toUpperFirstChar(field.getName());
        lookupElements.add(
                LookupElementBuilder.create("set" + preparedNameField + "()")
                        .withPresentableText("set" + preparedNameField + "($" + field.getName() + ")")
                        .withIcon(field.getIcon())
        );
        lookupElements.add(
                LookupElementBuilder.create("get" + preparedNameField + "()")
                        .withPresentableText("get" + preparedNameField + "()")
                        .withIcon(field.getIcon())
        );

        lookupElements.add(
                LookupElementBuilder.create("has" + preparedNameField + "()")
                        .withPresentableText("has" + preparedNameField + "()")
                        .withIcon(field.getIcon())
        );
        return lookupElements;
    }
}
