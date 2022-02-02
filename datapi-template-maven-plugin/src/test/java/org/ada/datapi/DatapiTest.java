package org.ada.datapi;

import org.ada.datapi.util.TestUtil;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DatapiTest {

    @Test
    public void testCannotInstantiate() throws Exception {
        assertTrue(TestUtil.testUtilClass(Datapi.class));
    }

    @Test
    public void main_invalid_arg() {
        // This test doesn't really work, I need to monitor
        // the system exit, hmm....
        Datapi.main(new String[] {});
    }

}
