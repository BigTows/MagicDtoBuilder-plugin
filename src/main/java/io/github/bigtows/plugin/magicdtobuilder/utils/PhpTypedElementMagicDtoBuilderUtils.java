package io.github.bigtows.plugin.magicdtobuilder.utils;

import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import io.github.bigtows.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class PhpTypedElementMagicDtoBuilderUtils {

    @Nullable
    @Deprecated
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

    @Nullable
    public static String getDtoNameByPhpTypedElement(PhpTypedElement typedElement) {
        Set<String> types = typedElement.getDeclaredType().getTypesSorted();
        String signatureMagicDtoBuilder = MagicDtoBuilderSettings.getInstance(typedElement.getProject())
                .getSignatureMagicDtoBuilder();
        List<String> result = new ArrayList<>();
        boolean hasMagicPartDtoBuilder = false;
        for (String type : types) {
            if (!hasMagicPartDtoBuilder && type.contains(signatureMagicDtoBuilder)) {
                hasMagicPartDtoBuilder = true;
                continue;
            }
            if (type.length() > 2) {
                if (!type.substring(0, 2).equals("#M") && !type.substring(0, 2).equals("#C")) {
                    result.add(type);
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
}
