/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package io.github.bigtows.plugin.magicdtobuilder.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import io.github.bigtows.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;
import io.github.bigtows.plugin.magicdtobuilder.utils.MagicMethodDtoBuilderUtils;
import io.github.bigtows.plugin.magicdtobuilder.utils.MethodReferenceUtils;
import io.github.bigtows.plugin.magicdtobuilder.utils.PhpTypedElementMagicDtoBuilderUtils;
import io.github.bigtows.plugin.magicdtobuilder.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
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
        if (psiElement.getPrevSibling() instanceof PhpTypedElement) {
            this.addCompletionsOfPhpTypedElement((PhpTypedElement) psiElement.getPrevSibling(), result);
        }
    }

    /**
     * Process Php typed element
     *
     * @param phpTypedElement php typed element
     * @param resultSet       completion set
     */
    private void addCompletionsOfPhpTypedElement(PhpTypedElement phpTypedElement, CompletionResultSet resultSet) {
        PhpClass phpClass = this.getBuilderDtoClassByPhpTypedElement(phpTypedElement);
        if (phpClass != null) {
            String fqnOfDto = PhpTypedElementMagicDtoBuilderUtils.getBuilderDtoNameByPhpTypedElement(phpTypedElement);
            String signatureMagicDtoBuilder = MagicDtoBuilderSettings.getInstance(phpClass.getProject()).getSignatureMagicDtoBuilder();
            phpClass.getFields().forEach(field -> resultSet.addAllElements(
                    this.createAccessorMethodLookupElementByField(field, signatureMagicDtoBuilder + "|" + fqnOfDto)
            ));
        }
        phpClass = this.getDtoClassByPhpTypedElement(phpTypedElement);
        if (phpClass != null) {
            phpClass.getFields().forEach(field -> resultSet.addAllElements(
                    this.createProviderMethodLookupElementByField(field)
            ));
        }
    }

    /**
     * Get PHPClass DTO by psi element (MethodReference, Variable)
     *
     * @param phpTypedElement php typed element
     * @return if success PHPClass else {@code null}
     */
    @Nullable
    private PhpClass getBuilderDtoClassByPhpTypedElement(@NotNull PhpTypedElement phpTypedElement) {
        String FQN = null;
        if (phpTypedElement instanceof MethodReference) {
            FQN = this.getFQNByMethodReference((MethodReference) phpTypedElement);
        } else if (phpTypedElement instanceof Variable) {
            FQN = PhpTypedElementMagicDtoBuilderUtils.getBuilderDtoNameByPhpTypedElement(phpTypedElement);
        }
        return this.getAnyClassByFQN(phpTypedElement.getProject(), FQN);
    }

    /**
     * Get PHPClass DTO by psi element (MethodReference, Variable)
     *
     * @param phpTypedElement php typed element
     * @return if success PHPClass else {@code null}
     */
    @Nullable
    private PhpClass getDtoClassByPhpTypedElement(@NotNull PhpTypedElement phpTypedElement) {
        String FQN = null;
        MagicDtoBuilderSettings settings = MagicDtoBuilderSettings.getInstance(phpTypedElement.getProject());
        if (phpTypedElement.getDeclaredType().getTypesSorted().stream()
                .noneMatch(type -> type.equals(settings.getSignatureMagicDtoBuilder()))) {
            if (phpTypedElement instanceof MethodReference || phpTypedElement instanceof Variable) {
                FQN = PhpTypedElementMagicDtoBuilderUtils.getDtoNameByPhpTypedElement(phpTypedElement);
            }
        }
        return this.getAnyClassByFQN(phpTypedElement.getProject(), FQN);
    }

    /**
     * Get any class by FQN
     *
     * @param project project
     * @param FQN     FQN
     * @return PhpClass of NULL
     */
    @Nullable
    private PhpClass getAnyClassByFQN(@NotNull Project project, @Nullable String FQN) {
        if (FQN != null) {
            final PhpIndex phpIndex = PhpIndex.getInstance(project);
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
            } else {
                return PhpTypedElementMagicDtoBuilderUtils.getBuilderDtoNameByPhpTypedElement(methodReference);
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


    private List<LookupElement> createAccessorMethodLookupElementByField(@NotNull Field field, String returnTypeSetter) {
        List<LookupElement> lookupElements = new ArrayList<>();
        String preparedNameField = StringUtils.toUpperFirstChar(field.getName());
        Icon publicMethodIcon = MagicDtoBuilderSettings.getPublicMethodIcon();
        lookupElements.add(
                LookupElementBuilder.create("set" + preparedNameField + "()")
                        .withPresentableText("set" + preparedNameField + "($" + field.getName() + ")")
                        .withTypeText(returnTypeSetter)
                        .withIcon(publicMethodIcon)
        );
        lookupElements.addAll(this.createProviderMethodLookupElementByField(field));
        return lookupElements;
    }

    /**
     * Create lookup elements by field
     *
     * @param field field on the basis of which creating lookup elements
     * @return lookup elements
     */
    private List<LookupElement> createProviderMethodLookupElementByField(@NotNull Field field) {
        List<LookupElement> lookupElements = new ArrayList<>();
        String preparedNameField = StringUtils.toUpperFirstChar(field.getName());
        Icon publicMethodIcon = MagicDtoBuilderSettings.getPublicMethodIcon();
        lookupElements.add(
                LookupElementBuilder.create("get" + preparedNameField + "()")
                        .withPresentableText("get" + preparedNameField + "()")
                        .withTypeText(field.getDeclaredType().toString())
                        .withIcon(publicMethodIcon)
        );

        lookupElements.add(
                LookupElementBuilder.create("has" + preparedNameField + "()")
                        .withPresentableText("has" + preparedNameField + "()")
                        .withTypeText(PhpType.BOOLEAN.toString())
                        .withIcon(publicMethodIcon)
        );
        return lookupElements;
    }
}
