package ru.uniteller.plugin.magicdtobuilder.utils;

import com.jetbrains.php.lang.psi.elements.PhpTypedElement;

import java.util.regex.Pattern;

public class PhpTypedElementMagicDtoBuilderUtils {


    public static String getDtoName(PhpTypedElement typedElement) {
        String[] types = typedElement.getDeclaredType().toString().split(Pattern.quote("|"));

        if (types[types.length - 1].equals("?")) {
            return types[types.length - 2];
        }else{
            return types[types.length - 1];
        }
    }
}
