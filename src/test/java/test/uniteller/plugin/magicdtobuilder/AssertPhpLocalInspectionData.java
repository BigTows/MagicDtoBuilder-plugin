/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.uniteller.plugin.magicdtobuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Assert php local inspection data.
 */
public final class AssertPhpLocalInspectionData {

    private final String templateDescription;

    private final Integer offsetTextProblem;

    private final List<Class> storageQuickFixClasses = new ArrayList<>();

    /**
     * Instantiates a new Assert php local inspection data.
     *
     * @param templateDescription the template description
     * @param offsetTextProblem   the offset text problem
     */
    public AssertPhpLocalInspectionData(final String templateDescription, Integer offsetTextProblem) {
        this.templateDescription = templateDescription;
        this.offsetTextProblem = offsetTextProblem;
    }

    /**
     * Gets template description.
     *
     * @return the template description
     */
    public String getTemplateDescription() {
        return templateDescription;
    }

    /**
     * Gets offset text problem.
     *
     * @return the offset text problem
     */
    public int getOffsetTextProblem() {
        return offsetTextProblem;
    }

    /**
     * Add quick fix classes assert php local inspection data.
     *
     * @param clazz the clazz
     * @return the assert php local inspection data
     */
    public AssertPhpLocalInspectionData addQuickFixClasses(Class clazz) {
        storageQuickFixClasses.add(clazz);
        return this;
    }

    /**
     * Gets storage quick fix classes.
     *
     * @return the storage quick fix classes
     */
    public List<Class> getStorageQuickFixClasses() {
        return storageQuickFixClasses;
    }

    /**
     * Build assert php local inspection data.
     *
     * @param templateDescription the template description
     * @param offsetTextProblem   the offset text problem
     * @return the assert php local inspection data
     */
    public static AssertPhpLocalInspectionData build(final String templateDescription, Integer offsetTextProblem) {
        return new AssertPhpLocalInspectionData(templateDescription, offsetTextProblem);
    }
}
