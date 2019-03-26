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

import java.util.Arrays;

/**
 * The type Php class utils.
 */
public class PhpClassUtils {


    /**
     * Is php class extended abstract dto boolean.
     *
     * @param phpClass the php class
     * @return the boolean
     */
    public static boolean isPhpClassExtendedAbstractDto(PhpClass phpClass) {
        String fqnAbstractDto = MagicDtoBuilderSettings.getInstance(phpClass.getProject()).getSignatureAbstractDto();
        return isPhpClassExtendedFQNClass(phpClass, fqnAbstractDto);
    }

    /**
     * Is php class extended fqn class boolean.
     *
     * @param phpClass the php class
     * @param FQN      the fqn
     * @return the boolean
     */
    public static boolean isPhpClassExtendedFQNClass(PhpClass phpClass, String FQN) {
        for (PhpClass superPhpClass : phpClass.getSupers()) {
            if (superPhpClass.getDeclaredType().toString().equals(FQN)) {
                return true;
            }

            if (Arrays.stream(superPhpClass.getSupers()).anyMatch(superSuperPhpClass -> isPhpClassExtendedFQNClass(superSuperPhpClass, FQN))) {
                return true;
            }
        }
        return false;
    }
}
