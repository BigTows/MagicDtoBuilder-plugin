package ru.uniteller.plugin.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpParameterBasedTypeProvider;
import org.jetbrains.annotations.NotNull;

public class MagicMethodDtoBuilderAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!PhpParameterBasedTypeProvider.isMeta(element)) {
            element.accept(new PhpMagicMethodDtoBuilderVisitor(holder));
        }
    }
}
