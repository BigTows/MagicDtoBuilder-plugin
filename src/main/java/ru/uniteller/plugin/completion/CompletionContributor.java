package ru.uniteller.plugin.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class CompletionContributor extends com.intellij.codeInsight.completion.CompletionContributor {

    public CompletionContributor() {
        final PsiElementPattern.Capture<PsiElement> capture = psiElement().afterLeaf(psiElement(PhpTokenTypes.ARROW));
        extend(CompletionType.BASIC, capture, new CompletionProvider<CompletionParameters>() {

            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                
            }
        });
    }
}
