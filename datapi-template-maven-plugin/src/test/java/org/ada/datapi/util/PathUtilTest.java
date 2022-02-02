package org.ada.datapi.util;

import org.ada.datapi.exception.DatapiException;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PathUtilTest {

    @Test
    public void testCannotInstantiate() throws Exception {
        assertTrue(TestUtil.testUtilClass(PathUtil.class));
    }

    @Test
    public void normalize_path_with_file() throws Exception {
        assertThat(PathUtil.normalize("/src/main/something.java", "."), equalTo("src.main.something.java"));
    }

    @Test
    public void normalize_path_without_file() throws Exception {
        assertThat(PathUtil.normalize("/src/main/", "/"), equalTo("src/main"));
    }

    @Test
    public void convertSystemPathToJavaPath_null() throws Exception {
        MatcherAssert.assertThat(PathUtil.pathToPackage(null, "anything"), equalTo(null));
    }

    @Test
    public void convertSystemPathToJavaPath_empty() throws Exception {
        MatcherAssert.assertThat(PathUtil.pathToPackage("", "anything"), equalTo(""));

    }

    @Test
    public void convertSystemPathToJavaPath_linux() throws Exception {
        String absolutePath = "somewhere/project/module/src/main/java/com/example/somewhere/Something.java";

        String convertedPath = PathUtil.pathToPackage(absolutePath, "src/main/java");

        MatcherAssert.assertThat(convertedPath, equalTo("com.example.somewhere.Something"));
    }

    @Test
    public void convertSystemPathToJavaPath_windows() throws Exception {
        String absolutePath = "somewhere\\project\\module\\src\\main\\java\\com\\example\\somewhere\\Something.java";

        String convertedPath = PathUtil.pathToPackage(absolutePath, "src/main/java");

        MatcherAssert.assertThat(convertedPath, equalTo("com.example.somewhere.Something"));
    }

    @Test(expected = DatapiException.class)
    public void convertSystemPathToJavaPath_root_not_contained() throws Exception {
        String absolutePath = "somewhere/project/module/src/main/java/com/example/somewhere/Something.java";

        PathUtil.pathToPackage(absolutePath, "false/root");
    }

}