package org.ada.datapi.data;

import org.ada.datapi.exception.DatapiException;
import org.junit.Test;

import static org.ada.datapi.data.Mode.GENERATE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ModeTest {

    @Test(expected = IllegalArgumentException.class)
    public void getMode_null() throws Exception {
        Mode.getMode(null);
    }

    @Test
    public void getMode_WeiRD_CasES() throws Exception {
        Mode foundMode = Mode.getMode("geNErate");

        assertThat(foundMode, equalTo(GENERATE));
    }

    @Test(expected = DatapiException.class)
    public void getMode_no_result() throws Exception {
        Mode.getMode("this will return no results");
    }

}