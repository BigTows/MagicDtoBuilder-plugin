package ru.uniteller.plugin.annotator;

import com.intellij.codeInsight.daemon.impl.AnnotationHolderImpl;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import ru.uniteller.plugin.resolver.reference.MagicMethodDtpBuilderReferenceResolver;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Method visitor for magic methods dto builder.
 */
public class PhpMagicMethodDtoBuilderVisitor extends PhpElementVisitor {

    /**
     * Annotation holder
     */
    private final AnnotationHolder annotationHolder;

    /**
     * Instantiates a new Php magic method dto builder visitor.
     *
     * @param annotationHolder the annotation holder
     */
    public PhpMagicMethodDtoBuilderVisitor(AnnotationHolder annotationHolder) {
        this.annotationHolder = annotationHolder;
    }

    @Override
    public void visitPhpMethodReference(MethodReference reference) {
        if (!this.isMagicSetterMethodDtoBuilder(reference)) {
            return;
        }

        this.checkSetterMagicOnMaintenanceParams(reference);

        if (annotationHolder instanceof AnnotationHolderImpl) {
            this.removeAnnotationAboutMemberHasPrivateAccess((AnnotationHolderImpl) annotationHolder);
        }

        super.visitPhpMethodReference(reference);
    }

    /**
     * Check setter magic on maintenance params
     *
     * @param reference method reference (setter magic)
     */
    private void checkSetterMagicOnMaintenanceParams(MethodReference reference) {
        if (reference.getParameterList() != null && reference.getParameters().length == 0) {
            annotationHolder.createErrorAnnotation(reference.getParameterList(), "Параметры забыл...");
        }
    }

    /**
     * Detect setter method of magic dto builder
     *
     * @param methodReference method reference
     * @return {@code true} if is setter method and is part of magic dto builder
     */
    private boolean isMagicSetterMethodDtoBuilder(MethodReference methodReference) {
        String methodName = methodReference.getName();
        if (null == methodName || methodName.length() < 4) {
            return false;
        }
        return methodReference.getSignature().contains(MagicMethodDtpBuilderReferenceResolver.SIGNATURE_METHOD_CREATE)
                && methodName.substring(0, 3).equals("set");
    }


    /**
     * Remove annotation about member has private access
     *
     * @param annotations annotations holder
     */
    @Deprecated
    private void removeAnnotationAboutMemberHasPrivateAccess(AnnotationHolderImpl annotations) {
        removeElementAtList(annotations, annotations.stream()
                .filter(annotation ->
                        annotation.getMessage() != null && annotation.getMessage().equals("Member has private access"))
                .collect(Collectors.toList()));
    }

    private void removeElementAtList(AnnotationHolderImpl holder, List<Annotation> set) {
        if (set.size() == 0) {
            return;
        }
        holder.remove(set.get(0));
        set.remove(0);
        removeElementAtList(holder, set);
    }
}
