package test.uniteller.plugin.magicdtobuilder.utils;

import io.github.bigtows.plugin.magicdtobuilder.utils.StringUtils;
import org.junit.Assert;
import test.uniteller.plugin.magicdtobuilder.BaseTestIntellij;

public class TestUtils extends BaseTestIntellij {

    public void testStringUtils() {
        Assert.assertEquals("A", StringUtils.toUpperFirstChar("a"));
        Assert.assertEquals("BA", StringUtils.toUpperFirstChar("bA"));
        Assert.assertEquals("Url", StringUtils.toUpperFirstChar("url"));
    }
}
