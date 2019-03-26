package io.github.bigtows.plugin.magicdtobuilder.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class MagicMethodDtoCompletionContributor extends CompletionContributor {
    /**
     * Instantiates a new Magic method completion contributor.
     */
    public MagicMethodDtoCompletionContributor() {
        final PsiElementPattern.Capture<PsiElement> capture = psiElement().afterLeaf(psiElement(PhpTokenTypes.ARROW));
        extend(CompletionType.BASIC, capture, new MagicMethodDtoCompletionProvider());
    }
}
