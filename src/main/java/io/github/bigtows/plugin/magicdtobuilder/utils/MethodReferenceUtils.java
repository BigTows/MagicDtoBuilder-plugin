/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package io.github.bigtows.plugin.magicdtobuilder.utils;

import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.elements.Variable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Collection methods for better experience for work with psi elements
 */
public final class MethodReferenceUtils {

    /**
     * Return root method reference
     *
     * @param methodReference method reference
     * @return root method reference
     * <p>
     * if put AnyObject::add("data")->setData("data")->setA(1);
     * return AnyObject::add("data")
     * </p>
     */
    public static MethodReference getFirstMethodReference(MethodReference methodReference) {
        PsiElement buffer = methodReference;
        while (buffer != null) {
            try {
                if (buffer.getFirstChild() instanceof MethodReference) {
                    buffer = buffer.getFirstChild();
                } else {
                    break;
                }
            } catch (Throwable e) {
                break;
            }
        }
        return (MethodReference) buffer;
    }

    /**
     * Return declared type at root
     *
     * @param methodReference method reference
     * @return declared type at root
     */
    public static PhpTypedElement getPhpTypedElementAtRootByMethodReference(@NotNull MethodReference methodReference) {
        PhpTypedElement buffer = methodReference;

        while (true) {
            PsiElement firstPsiChild = buffer.getFirstPsiChild();
            if (firstPsiChild instanceof MethodReference || firstPsiChild instanceof Variable) {
                buffer = (PhpTypedElement) firstPsiChild;
            } else {
                break;
            }
        }
        return buffer;
    }

    /**
     * Try get resolve for method reference
     *
     * @param methodReference target for searching resolve
     * @return if can't get resolve for method reference, return {@code null} else PsiElement
     */
    @Nullable
    public static PsiElement getResolvePsiElementAtMethodReference(MethodReference methodReference) {
        try {
            return methodReference.resolve();
        } catch (IndexNotReadyException e) {
            return null;
        }
    }


    /**
     * Return name field class by accessors method reference
     *
     * @param providerMethodReference method class like: "set", "get", "has"
     * @return name of field
     */
    @Nullable
    public static String getNameFieldByAccessorMethod(MethodReference providerMethodReference) {
        String nameField = providerMethodReference.getName();
        if (null == nameField || nameField.length() < 4) {
            return null;
        }
        StringBuilder nameFieldBuilder = new StringBuilder(nameField);
        if (!MethodReferenceUtils.isPrefixAccessorMethod(nameFieldBuilder.substring(0, 3))) {
            return null;
        }
        nameFieldBuilder.delete(0, 3);
        return Character.toLowerCase(nameFieldBuilder.charAt(0)) + nameFieldBuilder.substring(1);
    }


    /**
     * Return name field class by provider method reference
     *
     * @param providerMethodReference method class like: "getter", "has"
     * @return name of field
     */
    @Nullable
    public static String getNameFieldByProviderMethod(MethodReference providerMethodReference) {
        String nameField = providerMethodReference.getName();
        if (nameField == null) {
            return null;
        }
        if (!MethodReferenceUtils.isPrefixProviderMethod(nameField)) {
            return null;
        }
        nameField = nameField.substring(3);
        return StringUtils.toLowerFirstChar(nameField);
    }

    /**
     * Detect is prefix accessor
     *
     * @param prefix prefix of method
     * @return {@code true} if prefix same provider method else {@code false}
     */
    private static boolean isPrefixAccessorMethod(String prefix) {
        return prefix.equals("set") || isPrefixProviderMethod(prefix);
    }

    /**
     * Detect is prefix provider
     *
     * @param methodName method name
     * @return {@code true} if prefix same provider method else {@code false}
     */
    private static boolean isPrefixProviderMethod(String methodName) {
        return methodName.startsWith("get") || methodName.startsWith("has");
    }
}
