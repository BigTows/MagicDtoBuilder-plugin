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

import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import io.github.bigtows.plugin.magicdtobuilder.providers.type.DtoBuilderTypeProvider;
import io.github.bigtows.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utils helpful methods for php typed elements
 */
public final class PhpTypedElementMagicDtoBuilderUtils {

    /**
     * Get Builder DTO name by typed php element.
     *
     * @param typedElement typed php element
     * @return name of dto, or {@code null} if can't get
     */
    @Nullable
    public static String getBuilderDtoNameByPhpTypedElement(PhpTypedElement typedElement) {
        Set<String> types = typedElement.getDeclaredType().getTypesSorted();
        String signatureMagicDtoBuilder = MagicDtoBuilderSettings.getInstance(typedElement.getProject())
                .getSignatureMagicDtoBuilder();
        List<String> result = new ArrayList<>();
        boolean hasMagicPartDtoBuilder = false;
        for (String type : types) {
            if (type.contains(signatureMagicDtoBuilder)) {
                hasMagicPartDtoBuilder = true;
                continue;
            }
            if (type.length() > 2) {
                if (!type.startsWith("#M") && !type.startsWith("#C") && type.startsWith("#" + DtoBuilderTypeProvider.SIGNATURE_KEY)) {
                    result.add(type.substring(2));
                }
            } else {
                result.add(type);
            }
        }
        if (result.size() == 1 && hasMagicPartDtoBuilder) {
            return result.get(0);
        } else {
            return null;
        }
    }

    /**
     * Get DTO name by typed php element.
     *
     * @param typedElement typed php element
     * @return name of dto, or {@code null} if can't get
     */
    @Nullable
    public static String getDtoNameByPhpTypedElement(PhpTypedElement typedElement) {
        Set<String> types = typedElement.getDeclaredType().getTypesSorted();
        String signatureMagicDtoBuilder = MagicDtoBuilderSettings.getInstance(typedElement.getProject())
                .getSignatureAbstractDto();
        PhpIndex phpIndex = PhpIndex.getInstance(typedElement.getProject());
        List<String> result = new ArrayList<>();
        for (String type : types) {
            result.addAll(phpIndex.getClassesByFQN(type).stream()
                    .filter(phpClass -> Arrays.stream(phpClass.getSupers()).anyMatch(superClass -> superClass.getFQN().equals(signatureMagicDtoBuilder)))
                    .map(PhpNamedElement::getFQN).collect(Collectors.toList()));
        }
        if (result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
