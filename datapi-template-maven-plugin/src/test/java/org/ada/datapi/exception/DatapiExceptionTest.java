package org.ada.datapi.exception;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class DatapiExceptionTest {

    @Test
    public void testDatapiException_with_msg() {
        String message = "Message";
        DatapiException plasterException = new DatapiException(message);

        assertThat(plasterException.getMessage(), equalTo(message));
    }

    @Test
    public void testDatapiException_with_msg_and_throwable() {
        String message = "Message";
        Throwable error = new IllegalAccessError("");
        DatapiException plasterException = new DatapiException(message, error);

        assertThat(plasterException.getMessage(), equalTo(message));
        assertThat(plasterException.getCause(), equalTo(error));
    }

}