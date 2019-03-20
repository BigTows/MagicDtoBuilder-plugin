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

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.util.List;

public class BaseTestIntellij extends LightCodeInsightFixtureTestCase {


    @Override
    protected String getTestDataPath() {
        return "src/test/resources";
    }

    /**
     * PHP Completion assert
     *
     * @param filePath      file to data.
     * @param lookupStrings excepted data
     */
    protected void assertPhpCompletionContains(String filePath, String... lookupStrings) {
        myFixture.configureByFile(filePath);
        myFixture.completeBasic();
        completionContainsAssert(lookupStrings, myFixture.getLookupElementStrings());
    }

    /**
     * Completion assert between myFixture and Excepted data
     *
     * @param exceptedLookupElementStrings excepted data
     * @param lookupElementStrings         myFixture lookup elements
     */
    private void completionContainsAssert(String[] exceptedLookupElementStrings, List<String> lookupElementStrings) {
        List<String> lookupElements = myFixture.getLookupElementStrings();
        if (lookupElements == null) {
            fail("Error completionContainsAssert. (myFixture.getLookupElementStrings return NULL)");
        }
        if (exceptedLookupElementStrings.length == 0 && lookupElements.size() != 0) {
            fail(String.format(
                    "Lookup elements constrains unnecessary data. Available: %s",
                    String.join(", ", lookupElements))
            );
        }
        for (String lookupString : exceptedLookupElementStrings) {
            if (!lookupElements.contains(lookupString)) {
                fail(
                        String.format("Can't found %s in lookup elements. Available: %s",
                                lookupString,
                                String.join(", ", lookupElements)
                        )
                );
            }
        }
    }

    /**
     * Empty test for remove exception about: "No tests found in ..."
     */
    public void test() {

    }
}
