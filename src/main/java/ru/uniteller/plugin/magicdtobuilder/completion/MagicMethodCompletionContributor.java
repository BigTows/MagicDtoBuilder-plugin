/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.uniteller.plugin.magicdtobuilder.completion;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Completion contributor for magic method dto builder
 */
public class MagicMethodCompletionContributor extends com.intellij.codeInsight.completion.CompletionContributor {


    /**
     * Instantiates a new Magic method completion contributor.
     */
    public MagicMethodCompletionContributor() {
        final PsiElementPattern.Capture<PsiElement> capture = psiElement().afterLeaf(psiElement(PhpTokenTypes.ARROW));
        extend(CompletionType.BASIC, capture, new MagicMethodCompletionProvider());
    }
}
