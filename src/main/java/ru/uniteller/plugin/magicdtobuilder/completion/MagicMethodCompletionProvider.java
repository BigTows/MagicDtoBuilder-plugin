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
import ru.uniteller.plugin.magicdtobuilder.utils.PhpTypedElementMagicDtoBuilderUtils;
import ru.uniteller.plugin.magicdtobuilder.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

        if (phpClass == null) {
            return;
        }
        phpClass.getFields().forEach(field ->
                result.addElement(this.createSetterMethodLookupElementByField(field))
        );
        phpClass.getFields().forEach(field ->
                result.addAllElements(this.createDataProviderMethodLookupElementByField(field))
        );


    }

    @Nullable
    private PhpClass getDtoClassByPsiElement(@Nullable PsiElement element) {
        String FQN = null;
        if (element instanceof MethodReference) {
            MethodReference methodReference = (MethodReference) element;
            if (MagicMethodDtoBuilderUtils.isMagicSetterMethodDtoBuilder(methodReference)) {
                String[] types = methodReference.getDeclaredType().toString().split(Pattern.quote("|"));
                //Check Array out bound..
                FQN = types[types.length - 2];
            } else if (MagicMethodDtoBuilderUtils.isCreateMethodDtoBuilder(methodReference)) {
                ClassConstantReference classConstantReference = (ClassConstantReference) (methodReference).getParameters()[0];
                if (classConstantReference.getClassReference() == null) {
                    return null;
                }
                FQN = classConstantReference.getClassReference().getDeclaredType().toString();

            }
        } else if (element instanceof Variable) {
            FQN = PhpTypedElementMagicDtoBuilderUtils.getDtoName((PhpTypedElement) element);
        }
        if (FQN != null) {
            final PhpIndex phpIndex = PhpIndex.getInstance(element.getProject());
            for (PhpClass phpClass : phpIndex.getClassesByFQN(FQN)) {
                //Need check size collection (>1)?
                return phpClass;
            }
        }
        return null;

    }

    /**
     * Create lookup elements by field
     *
     * @param field field on the basis of which creating lookup elements
     * @return lookup elements
     */
    private LookupElement createSetterMethodLookupElementByField(@NotNull Field field) {
        String preparedNameField = StringUtils.toUpperFirstChar(field.getName());

        return LookupElementBuilder.create("set" + preparedNameField + "()")
                .withPresentableText("set" + preparedNameField + "($" + field.getName() + ")")
                .withIcon(field.getIcon());
    }

    private List<LookupElement> createDataProviderMethodLookupElementByField(@NotNull Field field) {
        List<LookupElement> lookupElements = new ArrayList<>();
        String preparedNameField = StringUtils.toUpperFirstChar(field.getName());
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
