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
import ru.uniteller.plugin.magicdtobuilder.providers.type.DtoBuilderTypeProvider;
import ru.uniteller.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;
import ru.uniteller.plugin.magicdtobuilder.utils.MagicMethodDtoBuilderUtils;
import ru.uniteller.plugin.magicdtobuilder.utils.MethodReferenceUtils;
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
        if (parameters.getOriginalPosition() == null) {
            return;
        }
/*
        PsiElement arrowElement = parameters.getOriginalPosition().getPrevSibling();

        PsiElement element = arrowElement.getPrevSibling();
        final Project project = element.getProject();
        final PhpIndex phpIndex = PhpIndex.getInstance(project);

        if (element instanceof MethodReference) {
            MethodReference root = MethodReferenceUtils.getFirstMethodReference((MethodReference) element);
            String FQN = null;

            if (MagicMethodDtoBuilderUtils.isCreateMethodDtoBuilder(root)) {
                ClassConstantReference classConstantReference = (ClassConstantReference) root.getParameters()[0];
                if (classConstantReference.getClassReference() == null) {
                    return;
                }
                FQN = classConstantReference.getClassReference().getDeclaredType().toString();
            } else if (MagicMethodDtoBuilderUtils.isMagicSetterMethodDtoBuilder((MethodReference) element)) {
                String[] types = ((MethodReference) element).getDeclaredType().toString().split(Pattern.quote("|"));
                String type = types[types.length - 2];
                FQN = type.substring(0, type.length() - DtoBuilderTypeProvider.POSTFIX_BUILDER_DTO.length());
            }

            if (FQN != null) {
                for (PhpClass phpClass : phpIndex.getClassesByFQN(FQN)) {
                    phpClass.getFields().forEach(field ->
                            result.addElement(this.createSetterMethodLookupElementByField(field))
                    );
                    phpClass.getFields().forEach(field ->
                            result.addAllElements(this.createDataProviderMethodLookupElementByField(field))
                    );

                }
            }

        } else if (element instanceof Variable) {

        }
*/

        PhpClass phpClass = getDtoClassByPsiElement(parameters.getOriginalPosition());

        if (phpClass==null){
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
    private PhpClass getDtoClassByPsiElement(PsiElement element) {
        PsiElement arrowElement = element.getPrevSibling();
        PsiElement prevNamedElement = arrowElement.getPrevSibling();
        String FQN = null;
        if (prevNamedElement instanceof MethodReference) {
            if (MagicMethodDtoBuilderUtils.isMagicSetterMethodDtoBuilder((MethodReference) prevNamedElement)) {
                String[] types = ((MethodReference) element).getDeclaredType().toString().split(Pattern.quote("|"));
                String type = types[types.length - 2];
                FQN = type.substring(0, type.length() - DtoBuilderTypeProvider.POSTFIX_BUILDER_DTO.length());
            } else {
                MethodReference root = MethodReferenceUtils.getFirstMethodReference((MethodReference) prevNamedElement);
                if (MagicMethodDtoBuilderUtils.isCreateMethodDtoBuilder(root)) {
                    ClassConstantReference classConstantReference = (ClassConstantReference) root.getParameters()[0];
                    if (classConstantReference.getClassReference() == null) {
                        return null;
                    }
                    FQN = classConstantReference.getClassReference().getDeclaredType().toString();
                }
            }
        } else if (prevNamedElement instanceof Variable) {
            FQN = this.getClassesFQNByVariableMagicDtoBuilder((Variable) prevNamedElement);
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


    @Nullable
    private String getClassesFQNByVariableMagicDtoBuilder(Variable variable) {
        String[] stringTypes = variable.getDeclaredType().toString().split(Pattern.quote("|"));
        if (stringTypes.length != 3) {
            return null;
        }
        String signatureCreateMethod =
                MagicDtoBuilderSettings.getInstance(variable.getProject())
                        .getSignatureMethodMagicDtoBuilderCreate();
        if (!stringTypes[2].equals("?") || !stringTypes[0].equals(signatureCreateMethod)) {
            return null;
        }
        String FQN = stringTypes[1].substring(0, stringTypes[1].length() - DtoBuilderTypeProvider.POSTFIX_BUILDER_DTO.length());

        return FQN;
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
