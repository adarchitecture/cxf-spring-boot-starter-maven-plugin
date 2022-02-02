package org.ada.datapi.data;

import org.ada.datapi.util.TestUtil;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RegexTest {

    @Test
    public void testCannotInstantiate() throws Exception {
        assertTrue(TestUtil.testUtilClass(Regex.class));
    }

}
