package ru.uniteller.plugin.providers.type;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
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

        if (psiElement instanceof MethodReference && isMagicMethodDtoBuilder((MethodReference) psiElement)) {

            PhpClass phpClass = this.getPhpClassByParameterMagicMethodDtoBuilder((MethodReference) psiElement);
            if (phpClass == null) {
                return null;
            }
            return PhpType.builder().add(phpClass.getFQN() + "Builder").build();
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

    /**
     * Get php class by parameter of magic method dto builder
     *
     * @param methodReference method reference
     * @return PhpClass instance if can't found return {@code null}
     */
    @Nullable
    private PhpClass getPhpClassByParameterMagicMethodDtoBuilder(MethodReference methodReference) {
        PhpIndex phpIndex = PhpIndex.getInstance(methodReference.getProject());
        String FQN = methodReference.getParameters()[0].getText().replace("::class", "");
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
