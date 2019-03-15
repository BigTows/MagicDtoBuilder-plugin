package ru.uniteller.plugin.resolver.reference;

import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpReference;
import com.jetbrains.php.lang.psi.resolve.PhpReferenceResolver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Reference magic method for DtoBuilder
 */
public class MagicMethodDtpBuilderReferenceResolver implements PhpReferenceResolver {
    @Override
    @Nullable
    public Collection<? extends PhpNamedElement> resolve(PhpReference phpReference) {

        if (phpReference instanceof MethodReference){
            List<PhpNamedElement> result = new ArrayList<>();


            return result;
        }
        return null;
    }
}
