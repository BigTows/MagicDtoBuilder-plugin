package ru;

import com.intellij.codeInsight.daemon.impl.AnnotationHolderImpl;
import com.intellij.codeInspection.GlobalInspectionTool;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.annotator.PhpAnnotatorVisitor;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.resolve.types.PhpParameterBasedTypeProvider;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;

public class I implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!PhpParameterBasedTypeProvider.isMeta(element)) {
            element.accept(new PhpElementVisitor(){
                @Override
                public void visitPhpMethodReference(MethodReference reference) {
                    if (holder instanceof AnnotationHolderImpl){
                        for (int i = 0;i<((AnnotationHolderImpl) holder).size();i++){
                            Annotation ann = ((AnnotationHolderImpl) holder).get(i);
                            if (ann.getMessage()!=null && ann.getMessage().equals("Member has private access")){
                                ((AnnotationHolderImpl) holder).remove(i);
                            }
                        }
                    }

                    super.visitPhpMethodReference(reference);
                }
            });
        }
    }
}
