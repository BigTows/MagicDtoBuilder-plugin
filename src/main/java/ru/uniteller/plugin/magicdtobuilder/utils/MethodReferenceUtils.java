/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.uniteller.plugin.magicdtobuilder.utils;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.NotNull;

/**
 * Collection methods for better experience for work with method reference
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
            if (buffer.getFirstChild() instanceof MethodReference) {
                buffer = buffer.getFirstChild();
            } else {
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
    public static PhpType getDeclaredTypeAtRootByMethodReference(@NotNull MethodReference methodReference) {
        PhpTypedElement buffer = methodReference;

        while (true) {
            PsiElement firstPsiChild = buffer.getFirstPsiChild();
            if (firstPsiChild instanceof MethodReference || firstPsiChild instanceof Variable) {
                buffer = (PhpTypedElement) firstPsiChild;
            } else {
                break;
            }
        }
        return buffer.getDeclaredType();
    }
}
