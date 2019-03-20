/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.uniteller.plugin.magicdtobuilder.providers.type;

import com.google.errorprone.annotations.Var;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.ParameterImpl;
import com.jetbrains.php.lang.psi.elements.impl.VariableImpl;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider3;
import org.jetbrains.annotations.Nullable;
import ru.uniteller.plugin.magicdtobuilder.utils.MagicMethodDtoBuilderUtils;
import ru.uniteller.plugin.magicdtobuilder.utils.MethodReferenceUtils;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Type provider for magic dto builder
 */
public class DtoBuilderTypeProvider implements PhpTypeProvider3 {

    /**
     * Post data for DTO builder.
     * <p>
     * If we have Dto "ExampleDto", when builder Dto will be like "ExampleDto"+{@code POSTFIX_BUILDER_DTO}
     * </p>
     */
    public static final String POSTFIX_BUILDER_DTO = "Builder";

    @Override
    public char getKey() {
        return 'Đ';
    }

    @Nullable
    @Override
    public PhpType getType(PsiElement psiElement) {
        PhpType phpType = null;
        if (psiElement instanceof MethodReference) {
            MethodReference methodReference = (MethodReference) psiElement;
            if (MagicMethodDtoBuilderUtils.isCreateMethodDtoBuilder(methodReference)) {
                PhpClass phpClass = this.getPhpClassByParameterMagicMethodDtoBuilder(methodReference);
                if (phpClass != null) {
                    phpType = PhpType.builder().add(phpClass.getFQN()).build();
                }
            } else if (MagicMethodDtoBuilderUtils.isMagicSetterMethodDtoBuilder(methodReference)) {
                phpType = PhpType.builder().add(
                        this.findMagicDtoBuilderByDeclarationType(MethodReferenceUtils.getDeclaredTypeAtRootByMethodReference(methodReference))
                ).build();
            } else if (MagicMethodDtoBuilderUtils.isMagicGetterMethodDtoBuilder(methodReference)) {
                //TODO exception.. To reindex this file IDEA has to be restarted
                PsiElement resolveElement = methodReference.resolve();
                if (resolveElement instanceof Field) {
                    phpType = PhpType.builder().add(((Field) resolveElement).getDeclaredType()).build();
                }
            } else if (MagicMethodDtoBuilderUtils.isMagicHasMethodDtoBuilder(methodReference)) {
                phpType = PhpType.BOOLEAN;
            }
        }

        return phpType;
    }

    /**
     * Get php class by parameter of magic method dto builder
     *
     * @param methodReference method reference
     * @return PhpClass instance if can't found return {@code null}
     */
    @Nullable
    private PhpClass getPhpClassByParameterMagicMethodDtoBuilder(MethodReference methodReference) {
        PhpIndex phpIndex = PhpIndex.getInstance(methodReference.getProject());
        String FQN = ((ClassReference) methodReference.getParameters()[0].getFirstChild()).getDeclaredType().toString();
        try {
            for (PhpClass phpClass : phpIndex.getClassesByFQN(FQN)) {
                return phpClass;
            }
        } catch (IndexNotReadyException ignore) {
        }
        return null;
    }

    private String findMagicDtoBuilderByDeclarationType(PhpType declaredType) {
        String[] types = declaredType.toString().split(Pattern.quote("|"));

        for (int i = types.length - 1; i != 0; i--) {
            if (!types[i].equals("?")) {
                return types[i];
            }
        }
        return "";
    }


    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String s, Set<String> set, int i, Project project) {
        return null;
    }
}
