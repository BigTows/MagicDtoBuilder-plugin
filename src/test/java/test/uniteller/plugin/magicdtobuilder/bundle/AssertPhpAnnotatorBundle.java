/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.uniteller.plugin.magicdtobuilder.bundle;

import com.intellij.lang.annotation.HighlightSeverity;

public final class AssertPhpAnnotatorBundle {

    private final String message;

    private final int startOffset;

    private final int endOffset;

    private final HighlightSeverity severity;

    public AssertPhpAnnotatorBundle(String message, int startOffset, int endOffset, HighlightSeverity severity) {
        this.message = message;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public HighlightSeverity getSeverity() {
        return severity;
    }

    public static AssertPhpAnnotatorBuilder builder() {
        return new AssertPhpAnnotatorBuilder();
    }

    public static class AssertPhpAnnotatorBuilder {
        private String message;

        private int startOffset;

        private int endOffset;

        private HighlightSeverity severity;

        public AssertPhpAnnotatorBuilder message(String message) {
            this.message = message;
            return this;
        }

        public AssertPhpAnnotatorBuilder startOffset(int startOffset) {
            this.startOffset = startOffset;
            return this;
        }

        public AssertPhpAnnotatorBuilder endOffset(int endOffset) {
            this.endOffset = endOffset;
            return this;
        }

        public AssertPhpAnnotatorBuilder severity(HighlightSeverity severity) {
            this.severity = severity;
            return this;
        }

        public AssertPhpAnnotatorBundle build() {
            return new AssertPhpAnnotatorBundle(message, startOffset, endOffset, severity);
        }
    }

    @Override
    public String toString() {
        return String.format("%sData: \n Message: %s\n StartOffset: %s\n EndOffset: %s\n HighlightSeverity: %s",
                getClass(), message, startOffset, endOffset, severity.toString());
    }
}
