package org.ada.datapi.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DependencyTest {

    @Test
    public void getTemplate() throws Exception {
        Dependency dependency = new Dependency("somewhere.Class");

        assertThat(dependency.getTemplate(), equalTo("import somewhere.Class;"));
    }

}