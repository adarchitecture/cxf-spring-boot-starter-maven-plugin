package org.ada.datapi.exception;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DeveloperExceptionTest {

    @Test
    public void constructor() {
        DeveloperException exception = new DeveloperException();
        assertThat(exception.getMessage(), equalTo("A developer messed up. Please contact them, yell at them, and make them buy you things"));
    }

}