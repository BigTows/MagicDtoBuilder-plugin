package test.uniteller.plugin.magicdtobuilder.inspection;

import com.intellij.codeInspection.ProblemDescriptor;
import test.uniteller.plugin.magicdtobuilder.BaseTestIntellij;
import test.uniteller.plugin.magicdtobuilder.bundle.AssertPhpLocalInspectionBundle;

/**
 * The type Test invalid dto local inspector.
 */
public class TestInvalidDtoLocalInspector extends BaseTestIntellij {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureByFile("libs/DtoBuilder.php");
        myFixture.configureByFile("libs/InvalidExampleDto.php");
        myFixture.configureByFile("libs/ExampleDto.php");
        warmUpPhpIndex();
    }


    /**
     * Test invalid dto for create magic dto builder.
     */
    public void testInvalidDtoForCreateMagicDtoBuilder() {
        assertPhpLocalInspectionContains(
                "data/inspection/InvalidDtoForCreateMagicDtoBuilder.php",
                AssertPhpLocalInspectionBundle.build("DTO must extended at \\App\\Library\\DtoBuilder\\AbstractDto", 112)
        );
    }

    /**
     * Test valid dto for create magic dto builder.
     */
    public void testValidDtoForCreateMagicDtoBuilder() {
        if (getPhpLocalInspectionContains("data/inspection/ValidDtoForCreateMagicDtoBuilder.php").size() != 0) {
            fail("Founded extra local inspection");
        }
    }
}
