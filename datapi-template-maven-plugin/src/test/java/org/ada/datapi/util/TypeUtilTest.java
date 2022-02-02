package org.ada.datapi.util;

import com.google.common.collect.Lists;
import org.ada.datapi.domain.Type;
import org.ada.datapi.exception.DatapiException;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class TypeUtilTest {

    @Test
    public void testCannotInstantiate() throws Exception {
        assertTrue(TestUtil.testUtilClass(TypeUtil.class));
    }

    @Test
    public void normalizeTypeString_null() throws Exception {
        assertThat(TypeUtil.normalizeTypeString(null), equalTo(""));
    }

    @Test
    public void normalizeTypeString_empty() throws Exception {
        assertThat(TypeUtil.normalizeTypeString(""), equalTo(""));
    }

    @Test
    public void normalizeTypeString_standard_var() throws Exception {
        assertThat(TypeUtil.normalizeTypeString("Integer"), equalTo("Integer"));
    }

    @Test
    public void normalizeTypeString_messedUp() throws Exception {
        String input = "map<customClass,    List<  other_class>>";
        String expected = "Map<CustomClass, List<OtherClass>>";

        assertThat(TypeUtil.normalizeTypeString(input), equalTo(expected));
    }

    @Test
    public void normalizeVariableName_null() throws Exception {
        assertThat(TypeUtil.normalizeVariableName(null), equalTo(""));
    }

    @Test
    public void normalizeVariableName_empty() throws Exception {
        assertThat(TypeUtil.normalizeVariableName(""), equalTo(""));
    }

    @Test
    public void normalizeVariableName_already_good() throws Exception {
        assertThat(TypeUtil.normalizeVariableName("alreadyGoodVarName"), equalTo("alreadyGoodVarName"));
    }

    @Test
    public void normalizeVariableName_bad_capitalization() throws Exception {
        assertThat(TypeUtil.normalizeVariableName("BadCapitalization"), equalTo("badCapitalization"));
    }

    @Test
    public void normalizeVariableName_snake_case() throws Exception {
        assertThat(TypeUtil.normalizeVariableName("should_be_camel_case"), equalTo("shouldBeCamelCase"));
    }

    @Test
    public void normalizeVariableName_bad_spacing() throws Exception {
        assertThat(TypeUtil.normalizeVariableName("should be camel case"), equalTo("shouldBeCamelCase"));
    }

    @Test
    public void normalizeVariableName_all_bad_things() throws Exception {
        assertThat(TypeUtil.normalizeVariableName("Should be_camelCase"), equalTo("shouldBeCamelCase"));
    }

    @Test
    public void splitToIndividualTypes_null() throws Exception {
        assertThat(TypeUtil.splitToIndividualTypes(null), equalTo(Lists.newArrayList()));
    }

    @Test
    public void splitToIndividualTypes_empty() throws Exception {
        assertThat(TypeUtil.splitToIndividualTypes(""), equalTo(Lists.newArrayList()));
    }

    @Test
    public void splitToIndividualTypes_standard_var() throws Exception {
        assertThat(TypeUtil.splitToIndividualTypes("Integer"), equalTo(Lists.newArrayList("Integer")));
    }

    @Test
    public void splitToIndividualTypes_multiple() throws Exception {
        List<String> expected = Lists.newArrayList("Map", "List", "Integer", "String");

        assertThat(TypeUtil.splitToIndividualTypes("Map<List<Integer>, String>"), equalTo(expected));
    }

    @Test
    public void mergeTypeStringAndListOfTypes_null_string() throws Exception {
        assertThat(TypeUtil.mergeTypeStringAndListOfTypes(null, Lists.newArrayList()), equalTo(""));
    }

    @Test
    public void mergeTypeStringAndListOfTypes_empty_string() throws Exception {
        assertThat(TypeUtil.mergeTypeStringAndListOfTypes("", Lists.newArrayList()), equalTo(""));
    }

    @Test(expected = DatapiException.class)
    public void mergeTypeStringAndListOfTypes_empty_list() throws Exception {
        TypeUtil.mergeTypeStringAndListOfTypes("Type", Lists.newArrayList());
    }

    @Test
    public void mergeTypeStringAndListOfTypes() throws Exception {
        String start = "Map<Something, somewhere<example>>";
        List<Type> types = Lists.newArrayList(
                new Type("Map", null),
                new Type("String", null),
                new Type("List", null),
                new Type("Integer", null));

        String expected = "Map<String, List<Integer>>";

        assertThat(TypeUtil.mergeTypeStringAndListOfTypes(start, types), equalTo(expected));
    }
}