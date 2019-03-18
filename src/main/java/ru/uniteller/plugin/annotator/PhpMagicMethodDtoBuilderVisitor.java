package ru.uniteller.plugin.annotator;

import com.intellij.codeInsight.daemon.impl.AnnotationHolderImpl;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;

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
//TODO....
     //   annotationHolder.createInfoAnnotation(reference,"a");
     /*   for (int i = 0; i < annotationHolder.size(); i++) {
            Annotation ann = annotationHolder.get(i);
            if (ann.getMessage() != null && ann.getMessage().equals("Member has private access")) {
                annotationHolder.remove(i);
            }
        }*/
     //annotationHolder.createWarningAnnotation()
     annotationHolder.createAnnotation(HighlightSeverity.INFO, reference.getTextRange(),"A");
        super.visitPhpMethodReference(reference);
    }

    private void removeAnnotationFromAnnotationHolderAtIndex(int index){

    }
}
