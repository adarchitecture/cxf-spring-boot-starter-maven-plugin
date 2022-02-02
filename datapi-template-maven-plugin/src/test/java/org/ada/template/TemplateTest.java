package org.ada.template;

import org.ada.datapi.template.Template;
import org.ada.datapi.util.TestUtil;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TemplateTest {

    @Test
    public void testCannotInstantiate() throws Exception {
        assertTrue(TestUtil.testUtilClass(Template.class));
    }

}
