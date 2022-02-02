package org.ada.datapi.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TypeTest {

    @Test
    public void getTemplate() throws Exception {
        Type type = new Type("Type");

        assertThat(type.getTemplate(), equalTo("Type"));
    }

}