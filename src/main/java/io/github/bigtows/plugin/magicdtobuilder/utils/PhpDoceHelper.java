package io.github.bigtows.plugin.magicdtobuilder.utils;

import com.jetbrains.php.lang.psi.elements.Method;

public class PhpDoceHelper {

    /**
     * Check php type returned in php doc
     *
     * @param method  method for checking
     * @param phpType searching php type
     * @return {@code true} if exits else {@code false}
     */
    public static boolean equalsReturnPhpTypeInMethod(Method method, String phpType) {
        if (null == method.getDocComment()) {
            return false;
        }

        if (null == method.getDocComment().getReturnTag()) {
            return false;
        }


        return method.getDocComment().getReturnTag().getDocType().toString().equals(phpType);
    }
}
