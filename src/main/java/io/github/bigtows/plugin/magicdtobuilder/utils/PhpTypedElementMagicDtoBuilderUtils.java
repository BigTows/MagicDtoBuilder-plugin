package io.github.bigtows.plugin.magicdtobuilder.utils;

import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import io.github.bigtows.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Utils helpful methods for php typed elements
 */
public final class PhpTypedElementMagicDtoBuilderUtils {

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
                .getSignatureMagicDtoBuilder();
        List<String> result = new ArrayList<>();
        boolean hasMagicPartDtoBuilder = false;
        for (String type : types) {
            if (type.contains(signatureMagicDtoBuilder)) {
                hasMagicPartDtoBuilder = true;
                continue;
            }
            if (type.length() > 2) {
                if (!type.startsWith("#M") && !type.startsWith("#C")) {
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
