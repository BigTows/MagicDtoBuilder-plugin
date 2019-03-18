package ru.uniteller.plugin.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.ClassConstantReference;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;
import ru.uniteller.plugin.resolver.reference.MagicMethodDtpBuilderReferenceResolver;
import ru.uniteller.plugin.utils.MethodReferenceUtils;

/**
 * Completion provider magic method.
 */
public class MagicMethodCompletionProvider extends CompletionProvider<CompletionParameters> {

    /**
     * TODO refactor code...
     * @param parameters
     * @param context
     * @param result
     */
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
        final PsiElement element = parameters.getPosition();
        final Project project = element.getProject();
        final PhpIndex phpIndex = PhpIndex.getInstance(project);
        PsiElement e = parameters.getOriginalPosition().getPrevSibling().getPrevSibling();
        if (e instanceof MethodReference) {
            MethodReference root = MethodReferenceUtils.getFirstMethodReference((MethodReference) e);
            if (root.getSignature().equals(MagicMethodDtpBuilderReferenceResolver.SIGNATURE_METHOD_CREATE)) {
                //Is magic dto builder.
                if (root.getParameters().length != 1 && !(root.getParameters()[0] instanceof ClassConstantReference)) {
                    return;
                }
                ClassConstantReference classConstantReference = (ClassConstantReference) root.getParameters()[0];
                String FQN = classConstantReference.getClassReference().getDeclaredType().toString();
                for (PhpClass phpClass : phpIndex.getClassesByFQN(FQN)) {
                    phpClass.getFields().forEach(field -> {
                                String nameField = Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
                                result.addElement(LookupElementBuilder.create("set" + nameField + "($data)"));
                                result.addElement(LookupElementBuilder.create("get" + nameField + "()"));
                                result.addElement(LookupElementBuilder.create("has" + nameField + "()").withPresentableText("has?"));
                            }
                    );
                }
            }
        }
        return;
    }
}
