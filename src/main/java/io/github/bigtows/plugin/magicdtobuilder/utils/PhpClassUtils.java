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

import com.jetbrains.php.lang.psi.elements.PhpClass;
import io.github.bigtows.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;

public class PhpClassUtils {


    public static boolean isPhpClassExtentedAbstractDto(PhpClass phpClass) {
        String fqnAbstractDto = MagicDtoBuilderSettings.getInstance(phpClass.getProject()).getSignatureAbstractDto();
        return isPhpClassExtentedFQNClass(phpClass, fqnAbstractDto);
    }

    public static boolean isPhpClassExtentedFQNClass(PhpClass phpClass, String FQN) {
        for (PhpClass superPhpClass : phpClass.getSupers()) {
            if (superPhpClass.getDeclaredType().toString().equals(FQN)) {
                return true;
            }
            if (phpClass.getSuperClass() != null && isPhpClassExtentedFQNClass(phpClass.getSuperClass(), FQN)) {
                return true;
            }
        }
        return false;
    }
}
