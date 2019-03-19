package ru.uniteller.plugin.magicdtobuilder.annotator;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import ru.uniteller.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;
import ru.uniteller.plugin.magicdtobuilder.utils.MagicMethodDtoBuilderUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    PhpMagicMethodDtoBuilderVisitor(AnnotationHolder annotationHolder) {
        this.annotationHolder = annotationHolder;
    }

    @Override
    public void visitPhpMethodReference(MethodReference reference) {
        if (!MagicMethodDtoBuilderUtils.isMagicSetterMethodDtoBuilder(reference)) {
            return;
        }

        this.checkSetterMagicOnMaintenanceParams(reference);

        if (annotationHolder instanceof Collection) {
            this.removeAnnotationAboutMemberHasPrivateAccess((Collection) annotationHolder);
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
            annotationHolder.createErrorAnnotation(reference.getTextRange().shiftLeft(1),
                    "Параметры забыл..."
            );
        }
    }

    /**
     * Remove annotation about member has private access
     *
     * @param annotations annotations holder
     */
    private void removeAnnotationAboutMemberHasPrivateAccess(Collection annotations) {
        List<Object> dataForRemove = new ArrayList<>();
        for (Object annotation : annotations) {
            if (annotation instanceof Annotation
                    && ((Annotation) annotation).getMessage() != null
                    && (((Annotation) annotation).getMessage().equals("Member has private access")
                    || ((Annotation) annotation).getMessage().equals("Member has protected access"))) {
                dataForRemove.add(annotation);
            }
        }
        removeElementAtList(annotations, dataForRemove);
    }

    /**
     * Remove object's from storage.
     *
     * @param storageData   storage with data
     * @param dataForRemove data for remove
     */
    private void removeElementAtList(Collection storageData, List dataForRemove) {
        if (dataForRemove.size() == 0) {
            return;
        }
        storageData.remove(dataForRemove.get(0));
        dataForRemove.remove(0);
        removeElementAtList(storageData, dataForRemove);
    }
}
