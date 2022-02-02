package org.ada.datapi.util;

import org.junit.ClassRule;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TestUtil {

    @ClassRule
    public static ExpectedException thrown = ExpectedException.none();

    /**
     * This ensures that a utility class does not have a public constructor and that it is annotated correctly
     */
    public static <T> boolean testUtilClass(Class<T> clazz) throws Exception {
        Constructor<?>[] cons = clazz.getDeclaredConstructors();
        cons[0].setAccessible(true);
        try {
            cons[0].newInstance((Object[]) null);
        } catch (InvocationTargetException e) {
            return e.getCause() instanceof UnsupportedOperationException
                    && e.getCause().getMessage().equals("This is a utility class and cannot be instantiated");
        }
        return false;
    }

}
