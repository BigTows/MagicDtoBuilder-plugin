package ru.uniteller.plugin.magicdtobuilder.resolver.reference;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.PhpReferenceResolver;
import org.jetbrains.annotations.Nullable;
import ru.uniteller.plugin.magicdtobuilder.providers.type.DtoBuilderTypeProvider;
import ru.uniteller.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;
import ru.uniteller.plugin.magicdtobuilder.utils.MethodReferenceUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Reference magic method for DtoBuilder
 */
public class MagicMethodDtoBuilderReferenceResolver implements PhpReferenceResolver {

    @Override
    @Nullable
    public Collection<? extends PhpNamedElement> resolve(PhpReference phpReference) {

        if (phpReference instanceof MethodReference) {
            MethodReference methodReference = (MethodReference) phpReference;
            PhpExpression phpExpression = methodReference.getClassReference();
            if (phpExpression != null && phpExpression.getDeclaredType().toString().contains(this.getSignatureMethodCreate(phpExpression))) {
                return this.getReferenceForMagicMethodBuilder(methodReference);
            }
        }
        return new ArrayList<>();
    }

    private List<PhpNamedElement> getReferenceForMagicMethodBuilder(MethodReference methodReference) {
        List<PhpNamedElement> phpNamedElements = new ArrayList<>();

        String methodName = this.getNameFieldByProvidersMethod(methodReference);
        if (null == methodName) {
            return phpNamedElements;
        }

        String[] declaredTypes = MethodReferenceUtils.getFirstMethodReference(methodReference).getDeclaredType().toString().split(Pattern.quote("|"));
        String declaredType = declaredTypes[declaredTypes.length - 2];
        String FQN = declaredType.substring(0, declaredType.length() - DtoBuilderTypeProvider.POSTFIX_BUILDER_DTO.length());
        PhpIndex phpIndex = PhpIndex.getInstance(methodReference.getProject());
        Collection<PhpClass> phpClasses = phpIndex.getClassesByFQN(FQN);

        phpClasses.forEach(phpClass -> {
            for (Field field : phpClass.getFields()) {
                if (field.getName().equals(methodName)) {
                    phpNamedElements.add(field);
                }
            }
        });
        return phpNamedElements;
    }

    /**
     * Return name field class by provider method reference
     *
     * @param providerMethodReference method class like: "set", "get", "has"
     * @return name of field
     * //TODO mb need, move to utils package
     */
    @Nullable
    private String getNameFieldByProvidersMethod(MethodReference providerMethodReference) {
        String nameField = providerMethodReference.getName();
        if (null == nameField || nameField.length() < 4) {
            return null;
        }
        StringBuilder nameFieldBuilder = new StringBuilder(nameField);
        if (!this.isPrefixProviderMethod(nameFieldBuilder.substring(0, 3))) {
            return null;
        }
        nameFieldBuilder.delete(0, 3);
        return Character.toLowerCase(nameFieldBuilder.charAt(0)) + nameFieldBuilder.substring(1);
    }

    /**
     * Detect is prefix provider
     *
     * @param prefix prefix of method
     * @return {@code true} if prefix same provider method else {@code false}
     */
    private boolean isPrefixProviderMethod(String prefix) {
        return prefix.equals("set") || prefix.equals("get") || prefix.equals("has");
    }

    /**
     * Get signature for method create magic dto builder
     *
     * @param element any Psi element for get ProjectInstance
     * @return signature for method
     */
    private String getSignatureMethodCreate(PsiElement element) {
        return MagicDtoBuilderSettings.getInstance(
                element.getProject()
        ).getSignatureMethodMagicDtoBuilderCreate();
    }
}
