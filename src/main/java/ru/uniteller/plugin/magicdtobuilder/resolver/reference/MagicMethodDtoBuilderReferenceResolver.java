/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.uniteller.plugin.magicdtobuilder.resolver.reference;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.PhpReferenceResolver;
import org.jetbrains.annotations.Nullable;
import ru.uniteller.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

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
            if (phpExpression != null && phpExpression.getDeclaredType().toString().contains(this.getSignatureMagicDtoBuilder(phpExpression))) {
                return this.getReferenceForMagicMethodBuilder(methodReference);
            }
        }
        return new ArrayList<>();
    }

    private List<PhpNamedElement> getReferenceForMagicMethodBuilder(MethodReference methodReference) {
        List<PhpNamedElement> phpNamedElements = new ArrayList<>();

        String methodName = this.getNameFieldByProvidersMethod(methodReference);
        if (null == methodName) {
            return phpNamedElements;
        }
        String[] declaredTypes = new String[0];
        PsiElement firstChildElement = methodReference.getFirstChild();
        if (firstChildElement instanceof MethodReference || firstChildElement instanceof Variable) {
            declaredTypes = ((PhpReference) firstChildElement).getDeclaredType().toString().split(Pattern.quote("|"));
        }
        String FQN;
        if (declaredTypes[declaredTypes.length - 1].equals("?")) {
            //is local variable
            FQN = declaredTypes[declaredTypes.length - 2];
        } else {
            FQN = declaredTypes[declaredTypes.length - 1];
        }
        PhpIndex phpIndex = PhpIndex.getInstance(methodReference.getProject());
        Collection<PhpClass> phpClasses = phpIndex.getClassesByFQN(FQN);

        phpClasses.forEach(phpClass -> {
            for (Field field : phpClass.getFields()) {
                if (field.getName().equals(methodName)) {
                    phpNamedElements.add(field);
                }
            }
        });

        return phpNamedElements;
    }

    /**
     * Return name field class by provider method reference
     *
     * @param providerMethodReference method class like: "set", "get", "has"
     * @return name of field
     * //TODO mb need, move to utils package
     */
    @Nullable
    private String getNameFieldByProvidersMethod(MethodReference providerMethodReference) {
        String nameField = providerMethodReference.getName();
        if (null == nameField || nameField.length() < 4) {
            return null;
        }
        StringBuilder nameFieldBuilder = new StringBuilder(nameField);
        if (!this.isPrefixProviderMethod(nameFieldBuilder.substring(0, 3))) {
            return null;
        }
        nameFieldBuilder.delete(0, 3);
        return Character.toLowerCase(nameFieldBuilder.charAt(0)) + nameFieldBuilder.substring(1);
    }

    /**
     * Detect is prefix provider
     *
     * @param prefix prefix of method
     * @return {@code true} if prefix same provider method else {@code false}
     */
    private boolean isPrefixProviderMethod(String prefix) {
        return prefix.equals("set") || prefix.equals("get") || prefix.equals("has");
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
