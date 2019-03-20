package ru.uniteller.plugin.magicdtobuilder.inspection.local;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import ru.uniteller.plugin.magicdtobuilder.inspection.local.fix.AppendDtoClassIntoPhpDocParamTagQuickFix;
import ru.uniteller.plugin.magicdtobuilder.settings.MagicDtoBuilderSettings;
import ru.uniteller.plugin.magicdtobuilder.utils.PhpTypedElementMagicDtoBuilderUtils;

public class NotFoundDtoTypeInPhpDocParamTagLocalInspector extends LocalInspectionTool {
    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(final @NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return this.buildPhpElementVisitor(holder);
    }

    private PsiElementVisitor buildPhpElementVisitor(ProblemsHolder holder) {
        return new PhpElementVisitor() {
            @Override
            public void visitPhpFunctionCall(FunctionReference reference) {

                Function function = (Function) reference.resolve();
                if (function == null) {
                    return;
                }
                String methodSignature = MagicDtoBuilderSettings.getInstance(reference.getProject()).getSignatureMagicDtoBuilder();
                for (PsiElement parameter : reference.getParameters()) {
                    if (!(parameter instanceof PhpTypedElement)) {
                        continue;
                    }
                    PhpTypedElement typedElement = (PhpTypedElement) parameter;
                    if (typedElement.getDeclaredType().toString().contains(methodSignature)) {
                        processMagicDtoBuilder(typedElement, function, holder);
                    }

                }
            }
        };
    }

    private void processMagicDtoBuilder(PhpTypedElement element, Function function, ProblemsHolder holder) {
        if (function.getDocComment() == null) {
            return;
        }
        String typeDto = PhpTypedElementMagicDtoBuilderUtils.getDtoName(element);
        String signatureMagicDtoBuilder = MagicDtoBuilderSettings.getInstance(element.getProject()).getSignatureMagicDtoBuilder();
        for (PhpDocParamTag docParamTag : function.getDocComment().getParamTags()) {
            if (typeDto != null
                    && docParamTag.getDeclaredType().toString().contains(signatureMagicDtoBuilder)
                    && !docParamTag.getDeclaredType().toString().contains(typeDto)) {
                holder.registerProblem(
                        docParamTag,
                        "Append type dto",
                        new AppendDtoClassIntoPhpDocParamTagQuickFix(typeDto)
                );
            }
        }
    }

}
