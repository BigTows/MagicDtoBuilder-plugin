package ru.uniteller.plugin.magicdtobuilder.utils;

import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import org.jetbrains.annotations.Nullable;
import ru.uniteller.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;

import java.util.regex.Pattern;

public class PhpTypedElementMagicDtoBuilderUtils {

    @Nullable
    public static String getDtoName(PhpTypedElement typedElement) {
        String[] stringTypes = typedElement.getDeclaredType().toString().split(Pattern.quote("|"));
        MagicDtoBuilderSettings settings = MagicDtoBuilderSettings.getInstance(typedElement.getProject());
        if (stringTypes[stringTypes.length - 1].equals("?")) {
            //is locale variable
            if (stringTypes.length == 3 && stringTypes[0].equals(settings.getSignatureMethodMagicDtoBuilderCreate())) {
                return stringTypes[1];
            }
        } else if (stringTypes.length == 2 && stringTypes[0].equals(settings.getSignatureMagicDtoBuilder())) {
            //is parameter variable
            return stringTypes[1];
        }
        return null;
    }
}
