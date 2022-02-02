package org.ada.datapi.method;

import org.ada.datapi.util.TestUtil;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GenerateTest {

    @Test
    public void testCannotInstantiate() throws Exception {
        assertTrue(TestUtil.testUtilClass(Modify.class));
    }

}
