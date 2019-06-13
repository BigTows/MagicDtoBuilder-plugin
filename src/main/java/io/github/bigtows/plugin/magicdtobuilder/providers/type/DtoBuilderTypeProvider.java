/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package io.github.bigtows.plugin.magicdtobuilder.providers.type;

import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.FunctionImpl;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider3;
import io.github.bigtows.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;
import io.github.bigtows.plugin.magicdtobuilder.utils.MagicMethodDtoBuilderUtils;
import io.github.bigtows.plugin.magicdtobuilder.utils.MethodReferenceUtils;
import io.github.bigtows.plugin.magicdtobuilder.utils.PhpDoceHelper;
import io.github.bigtows.plugin.magicdtobuilder.utils.PhpTypedElementMagicDtoBuilderUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * Type provider for magic dto builder
 */
public class DtoBuilderTypeProvider implements PhpTypeProvider3 {

    public static char SIGNATURE_KEY = '☘';

    @Override
    public char getKey() {
        return SIGNATURE_KEY;
    }

    @Nullable
    @Override
    public PhpType getType(PsiElement psiElement) {
        if (psiElement instanceof MethodReference) {
            return this.processMethodReference((MethodReference) psiElement);
        } else if (psiElement instanceof Parameter) {
            return this.processParameter((Parameter) psiElement);
        }
        return null;
    }

    /**
     * Process method reference and get PhpType
     *
     * @param methodReference method reference
     * @return if can't search PhpType return {@code null} else PhpType
     */
    @Nullable
    private PhpType processMethodReference(MethodReference methodReference) {
        PhpType phpType = null;
        MagicDtoBuilderSettings settings = MagicDtoBuilderSettings.getInstance(methodReference.getProject());
        if (MagicMethodDtoBuilderUtils.isCreateMethodDtoBuilder(methodReference)) {
            phpType = this.getPhpTypeOfPhpClassByParameterMagicMethodDtoBuilder(methodReference);
        } else if (MagicMethodDtoBuilderUtils.isMagicSetterMethodDtoBuilder(methodReference)) {
            phpType = PhpType.builder()
                    .add(settings.getSignatureMagicDtoBuilder())
                    .add(this.buildBuilderDtoSignature(
                            PhpTypedElementMagicDtoBuilderUtils.getBuilderDtoNameByPhpTypedElement(
                                    MethodReferenceUtils.getPhpTypedElementAtRootByMethodReference(methodReference)
                            ))
                    ).build();
        } else if (MagicMethodDtoBuilderUtils.isMagicGetterMethodDtoBuilder(methodReference)) {
            phpType = this.getPhpTypeOfMagicGetterMethodDtoBuilder(methodReference);
        } else if (MagicMethodDtoBuilderUtils.isMagicHasMethodDtoBuilder(methodReference)) {
            phpType = PhpType.BOOLEAN;
        }

        PsiElement resolvePsiElement = this.getResolvePsiElementAtMethodReference(methodReference);
        if (resolvePsiElement instanceof Method) {
            Method method = (Method) resolvePsiElement;
            if (method.getContainingClass().getFQN().equals(settings.getSignatureMagicDtoBuilder()) &&
                    (method.getDeclaredType().toString().equals(settings.getSignatureAbstractDto())
                            || PhpDoceHelper.equalsReturnPhpTypeInMethod(method, settings.getSignatureAbstractDto())
                    )) {
                phpType = PhpType.builder().add(PhpTypedElementMagicDtoBuilderUtils.getBuilderDtoNameByPhpTypedElement(
                        MethodReferenceUtils.getPhpTypedElementAtRootByMethodReference(methodReference)
                )).build();
            }
        }
        return phpType;
    }

    /**
     * Try get resolve for method reference
     *
     * @param methodReference target for searching resolve
     * @return if can't get resolve for method reference, return {@code null} else PsiElement
     */
    @Nullable
    private PsiElement getResolvePsiElementAtMethodReference(MethodReference methodReference) {
        try {
            return methodReference.resolve();
        } catch (com.intellij.openapi.project.IndexNotReadyException e) {
            return null;
        }
    }

    /**
     * Get php type of php class by parameter of magic method dto builder
     *
     * @param methodReference method reference
     * @return PhpType or  if can't found return {@code null}
     */
    @Nullable
    private PhpType getPhpTypeOfPhpClassByParameterMagicMethodDtoBuilder(MethodReference methodReference) {
        PhpIndex phpIndex = PhpIndex.getInstance(methodReference.getProject());
        String FQN = ((ClassReference) methodReference.getParameters()[0].getFirstChild()).getDeclaredType().toString();
        try {
            Optional<PhpClass> phpClassOptional = phpIndex.getClassesByFQN(FQN).stream().findFirst();
            if (phpClassOptional.isPresent()) {
                return PhpType.builder().add(this.buildBuilderDtoSignature(phpClassOptional.get().getFQN())).build();
            }
        } catch (IndexNotReadyException ignore) {
        }
        return null;
    }

    /**
     * Get PhpType of magic getter method dto builder
     *
     * @param methodReference method reference
     * @return PHPType or if can't find field {@code null}
     */
    @Nullable
    private PhpType getPhpTypeOfMagicGetterMethodDtoBuilder(MethodReference methodReference) {
        try {
            PsiElement resolveElement = methodReference.resolve();
            if (resolveElement instanceof Field) {
                return PhpType.builder().add(((Field) resolveElement).getDeclaredType()).build();
            }
        } catch (IndexNotReadyException ignore) {
        }
        return null;
    }


    private PhpType processParameter(Parameter parameter) {
        MagicDtoBuilderSettings settings = MagicDtoBuilderSettings.getInstance(parameter.getProject());
        if (!parameter.getDeclaredType().toString().equals(settings.getSignatureMagicDtoBuilder())
                || !(parameter.getParent().getParent().getClass().equals(FunctionImpl.class))) {
            return null;
        }

        Function function = (Function) parameter.getParent().getParent();
        PhpDocComment phpDocComment = function.getDocComment();
        if (phpDocComment == null) {
            return null;
        }
        return this.getTypeParameterByPapDocComment(parameter, phpDocComment);
    }

    private PhpType getTypeParameterByPapDocComment(Parameter parameter, PhpDocComment phpDocComment) {
        return PhpType.builder()
                .add(MagicDtoBuilderSettings.getInstance(parameter.getProject()).getSignatureMagicDtoBuilder())
                .add(this.buildBuilderDtoSignature("App\\Library\\ExampleApi\\InvalidExampleDto")
                ).build();
    }

    /**
     * Build signature for magic builder dto
     *
     * @param dto FQN of DTO
     * @return signature
     */
    private String buildBuilderDtoSignature(String dto) {
        return "#" + this.getKey() + dto;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String s, Set<String> set, int i, Project project) {
        //TODO ... i don't know.
        return PhpIndex.getInstance(project).getClassesByFQN(s.substring(2));
    }
}
