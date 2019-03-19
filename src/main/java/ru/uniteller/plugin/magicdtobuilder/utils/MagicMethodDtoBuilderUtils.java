package ru.uniteller.plugin.magicdtobuilder.utils;

import com.jetbrains.php.lang.psi.elements.MethodReference;
import ru.uniteller.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;

/**
 * Utils method's for best development
 */
public class MagicMethodDtoBuilderUtils {

    /**
     * Detect setter method of magic dto builder
     *
     * @param methodReference method reference
     * @return {@code true} if is setter method and is part of magic dto builder
     */
    public static boolean isMagicSetterMethodDtoBuilder(MethodReference methodReference) {
        String methodName = methodReference.getName();
        if (null == methodName || methodName.length() < 4) {
            return false;
        }
        String signatureMethodCreate = MagicDtoBuilderSettings.getInstance(
                methodReference.getProject()
        ).getSignatureMethodMagicDtoBuilderCreate();

        return methodReference.getSignature().contains(signatureMethodCreate) && methodName.substring(0, 3).equals("set");
    }
}
