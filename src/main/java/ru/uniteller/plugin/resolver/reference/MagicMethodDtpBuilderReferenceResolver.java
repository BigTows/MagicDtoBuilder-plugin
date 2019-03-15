package ru.uniteller.plugin.resolver.reference;

import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.PhpReferenceResolver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Reference magic method for DtoBuilder
 */
public class MagicMethodDtpBuilderReferenceResolver implements PhpReferenceResolver {

    /**
     * Signature for method create at DTO Builder
     * TODO replace this to settings
     */
    public static final String SIGNATURE_METHOD_CREATE = "#M#C\\App\\Library\\DtoBuilder\\DtoBuilder.create";

    @Override
    @Nullable
    public Collection<? extends PhpNamedElement> resolve(PhpReference phpReference) {

        if (phpReference instanceof MethodReference) {
            MethodReference methodReference = (MethodReference) phpReference;
            PhpExpression phpExpression = methodReference.getClassReference();
            if (phpExpression != null && phpExpression.getDeclaredType().toString().contains(SIGNATURE_METHOD_CREATE)) {
                return this.getReferenceForMagicMethodBuilder(methodReference);
            }
        }
        return null;
    }

    private List<PhpNamedElement> getReferenceForMagicMethodBuilder(MethodReference methodReference) {
        List<PhpNamedElement> phpNamedElements = new ArrayList<>();

        String methodName = methodReference.getName().replace("get", "").replace("set", "").replace("has", "");

        methodName = Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
        String nameOfDto = ((MethodReference) methodReference.getClassReference()).getParameters()[0].getText().replace("::class", "");
        PhpIndex phpIndex = PhpIndex.getInstance(methodReference.getProject());
        PhpClass phpClass = (PhpClass) phpIndex.getClassesByFQN(nameOfDto).toArray()[0];
        for (Field field : phpClass.getFields()) {
            if (field.getName().equals(methodName)) {
                phpNamedElements.add(field);
            }
        }
        return phpNamedElements;
    }
}
