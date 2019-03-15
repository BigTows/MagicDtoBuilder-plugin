package ru.uniteller.plugin.providers.type;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider3;
import org.jetbrains.annotations.Nullable;
import ru.uniteller.plugin.resolver.reference.MagicMethodDtpBuilderReferenceResolver;

import java.util.Collection;
import java.util.Set;

public class DtoBuilderTypeProvider implements PhpTypeProvider3 {
    @Override
    public char getKey() {
        return 'D';
    }

    @Nullable
    @Override
    public PhpType getType(PsiElement psiElement) {

        if (psiElement instanceof MethodReference) {
            MethodReference methodReference = (MethodReference) psiElement;
            PhpType phpType = null;
            if (isMagicMethodDtoBuilder(methodReference)) {
                PhpClass phpClass = this.getPhpClassByParameterMagicMethodDtoBuilder(methodReference);
                if (phpClass != null) {
                    phpType = PhpType.builder().add(phpClass.getFQN() + "Builder").build();
                }
            } else if (isMagicSetterMethodDtoBuilder(methodReference)) {
                phpType = PhpType.builder().add("\\App\\Library\\ExampleApi\\ExampleDto" + "Builder").build();
            }

            return phpType;
        }

        return null;
    }

    /**
     * Detect magic method dto builder
     *
     * @param methodReference method reference
     * @return {@code true} is magic method dto builder else {@code false}
     */
    private boolean isMagicMethodDtoBuilder(MethodReference methodReference) {
        return methodReference.getSignature().equals(MagicMethodDtpBuilderReferenceResolver.SIGNATURE_METHOD_CREATE) && methodReference.getParameters().length == 1;
    }

    private boolean isMagicSetterMethodDtoBuilder(MethodReference methodReference) {
        String data = methodReference.getText();
        return methodReference.getSignature().contains(MagicMethodDtpBuilderReferenceResolver.SIGNATURE_METHOD_CREATE) && methodReference.getName().contains("set");
    }

    /**
     * Get php class by parameter of magic method dto builder
     *
     * @param methodReference method reference
     * @return PhpClass instance if can't found return {@code null}
     */
    @Nullable
    private PhpClass getPhpClassByParameterMagicMethodDtoBuilder(MethodReference methodReference) {
        PhpIndex phpIndex = PhpIndex.getInstance(methodReference.getProject());
        String FQN = ((ClassReference)methodReference.getParameters()[0].getFirstChild()).getDeclaredType().toString();
        for (PhpClass phpClass : phpIndex.getClassesByFQN(FQN)) {
            return phpClass;
        }
        return null;
    }


    @Override

    public Collection<? extends PhpNamedElement> getBySignature(String s, Set<String> set, int i, Project project) {
        return null;
    }
}
