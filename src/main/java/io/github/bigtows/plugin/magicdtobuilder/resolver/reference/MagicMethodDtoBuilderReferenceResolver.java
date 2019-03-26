/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package io.github.bigtows.plugin.magicdtobuilder.resolver.reference;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.PhpReferenceResolver;
import io.github.bigtows.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.github.bigtows.plugin.magicdtobuilder.utils.MethodReferenceUtils;
import io.github.bigtows.plugin.magicdtobuilder.utils.PhpTypedElementMagicDtoBuilderUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reference magic method for DtoBuilder
 */
public class MagicMethodDtoBuilderReferenceResolver implements PhpReferenceResolver {

    @Override
    @Nullable
    public Collection<? extends PhpNamedElement> resolve(PhpReference phpReference) {
        if (phpReference instanceof MethodReference) {
            MethodReference methodReference = (MethodReference) phpReference;
            PhpExpression phpExpression = methodReference.getClassReference();
            if (phpExpression != null) {
                if (phpExpression.getDeclaredType().toString().contains(this.getSignatureMagicDtoBuilder(phpExpression))) {
                    return this.getReferenceForMagicMethodDtoBuilder(methodReference);
                }else if (phpExpression.getDeclaredType().toString().equals(PhpTypedElementMagicDtoBuilderUtils.getDtoNameByPhpTypedElement(phpExpression))){
                    return this.getReferenceForMagicMethodDtoBuilder(methodReference);
                }
            }
        }
        return new ArrayList<>();
    }


    /**
     * Get reference for magic method dto builder
     *
     * @param methodReference method reference
     * @return list of php named element
     */
    @NotNull
    private List<PhpNamedElement> getReferenceForMagicMethodDtoBuilder(MethodReference methodReference) {
        List<PhpNamedElement> phpNamedElements = new ArrayList<>();
        String methodName = MethodReferenceUtils.getNameFieldByAccessorMethod(methodReference);
        if (null == methodName) {
            return phpNamedElements;
        }
        PsiElement firstChildElement = methodReference.getFirstChild();
        if (firstChildElement instanceof PhpTypedElement) {
            this.findPhpClassInPhpTypedElement((PhpTypedElement) firstChildElement).forEach(phpClass ->
                    phpNamedElements.addAll(phpClass.getFields().stream().filter(field -> field.getName().equals(methodName))
                            .collect(Collectors.toList())
                    )
            );
        }
        return phpNamedElements;
    }

    /**
     * Find php classes form php typed element
     *
     * @param phpTypedElement php typed element
     * @return collection of php class
     */
    @NotNull
    private Collection<PhpClass> findPhpClassInPhpTypedElement(PhpTypedElement phpTypedElement) {
        String FQN = PhpTypedElementMagicDtoBuilderUtils.getBuilderDtoNameByPhpTypedElement(phpTypedElement);
        if (FQN == null){
            FQN = PhpTypedElementMagicDtoBuilderUtils.getDtoNameByPhpTypedElement(phpTypedElement);
        }
        PhpIndex phpIndex = PhpIndex.getInstance(phpTypedElement.getProject());
        return phpIndex.getClassesByFQN(FQN);
    }

    /**
     * Get signature for magic dto builder
     *
     * @param element any Psi element for get ProjectInstance
     * @return signature for method
     */
    private String getSignatureMagicDtoBuilder(PsiElement element) {
        return MagicDtoBuilderSettings.getInstance(
                element.getProject()
        ).getSignatureMagicDtoBuilder();
    }
}
