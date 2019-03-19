/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.uniteller.plugin.magicdtobuilder.utils;

import com.jetbrains.php.lang.psi.elements.ClassConstantReference;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import ru.uniteller.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;

/**
 * Utils method's for best development
 */
public class MagicMethodDtoBuilderUtils {

    /**
     * Detect magic method dto builder
     *
     * @param methodReference method reference
     * @return {@code true} is magic method dto builder else {@code false}
     */
    public static boolean isCreateMethodDtoBuilder(MethodReference methodReference) {
        String signatureMethodCreate = MagicDtoBuilderSettings.getInstance(
                methodReference.getProject()
        ).getSignatureMethodMagicDtoBuilderCreate();

        return methodReference.getSignature().equals(signatureMethodCreate)
                && methodReference.getParameters().length == 1
                && methodReference.getParameters()[0] instanceof ClassConstantReference;
    }

    /**
     * Detect any magic method in method reference
     *
     * @param methodReference method reference
     * @return {@code true} if detected, else {@code false}
     */
    public static boolean isMagicMethodDtoBuilder(MethodReference methodReference) {
        return isMagicSetterMethodDtoBuilder(methodReference)
                || isMagicGetterMethodDtoBuilder(methodReference)
                || isMagicHasMethodDtoBuilder(methodReference);
    }


    /**
     * Detect setter method of magic dto builder
     *
     * @param methodReference method reference
     * @return {@code true} if is setter method and is part of magic dto builder
     */
    public static boolean isMagicSetterMethodDtoBuilder(MethodReference methodReference) {
        return MagicMethodDtoBuilderUtils.hasMagicMethodDtoBuilderWithPrefix(methodReference, "set");
    }

    /**
     * Detect getter method of magic dto builder
     *
     * @param methodReference method reference
     * @return {@code true} if is getter method and is part of magic dto builder
     */
    public static boolean isMagicGetterMethodDtoBuilder(MethodReference methodReference) {
        return MagicMethodDtoBuilderUtils.hasMagicMethodDtoBuilderWithPrefix(methodReference, "get");
    }

    /**
     * Detect "has" method of magic dto builder
     *
     * @param methodReference method reference
     * @return {@code true} if is has method and is part of magic dto builder
     */
    public static boolean isMagicHasMethodDtoBuilder(MethodReference methodReference) {
        return MagicMethodDtoBuilderUtils.hasMagicMethodDtoBuilderWithPrefix(methodReference, "has");
    }

    /**
     * Check this method reference on accessibility: prefix and type method
     *
     * @param methodReference method reference
     * @param prefix          prefix
     * @return {@code true} if exists else {@code false}
     */
    private static boolean hasMagicMethodDtoBuilderWithPrefix(MethodReference methodReference, String prefix) {
        String methodName = methodReference.getName();
        if (null == methodName || methodName.length() < prefix.length() + 1) {
            return false;
        }
        String signatureMethodCreate = MagicDtoBuilderSettings.getInstance(
                methodReference.getProject()
        ).getSignatureMagicDtoBuilder();

        return methodReference.getSignature().contains(signatureMethodCreate) && methodName.substring(0, prefix.length()).equals(prefix);
    }
}
