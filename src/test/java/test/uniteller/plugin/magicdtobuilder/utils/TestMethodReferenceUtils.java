/*
 * Copyright (c) MagicDtoBuilder-plugin (2019)
 *
 * Authors:
 *    Andrey <and-rey2@yandex.ru> Malofeykin
 *    Alexander <gasfull98@gmail.com> Chapchuk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.uniteller.plugin.magicdtobuilder.utils;

import com.jetbrains.php.lang.psi.elements.MethodReference;
import io.github.bigtows.plugin.magicdtobuilder.utils.MethodReferenceUtils;
import org.junit.Assert;
import org.mockito.exceptions.base.MockitoException;
import test.uniteller.plugin.magicdtobuilder.BaseTestIntellij;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The type Test method reference utils.
 */
public class TestMethodReferenceUtils extends BaseTestIntellij {


    /**
     * Test method reference short name.
     */
    public void testMethodReferenceShortName() {
        MethodReference methodReference = mock(MethodReference.class);
        when(methodReference.getName()).thenReturn("a");
        Assert.assertNull(MethodReferenceUtils.getNameFieldByAccessorMethod(methodReference));
    }


    /**
     * Test method reference not provide method.
     */
    public void testMethodReferenceNotProvideMethod() {
        MethodReference methodReference = mock(MethodReference.class);
        when(methodReference.getName()).thenReturn("gesA");
        Assert.assertNull(MethodReferenceUtils.getNameFieldByAccessorMethod(methodReference));
    }


    /**
     * Test method reference throw on getting first child.
     */
    public void testMethodReferenceThrowOnGettingFirstChild() {
        MethodReference methodReference = mock(MethodReference.class);
        when(methodReference.getFirstChild()).thenThrow(new MockitoException(""));
        Assert.assertEquals(methodReference, MethodReferenceUtils.getFirstMethodReference(methodReference));
    }
}
