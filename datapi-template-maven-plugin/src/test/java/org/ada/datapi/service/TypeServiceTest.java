package org.ada.datapi.service;

import com.google.common.collect.Lists;
import org.ada.datapi.domain.Dependency;
import org.ada.datapi.domain.Type;
import org.ada.datapi.domain.TypeDeclaration;
import org.ada.datapi.exception.DatapiException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TypeServiceTest {

    @Mock
    private SearchService searchService;

    @Mock
    private DependencyService dependencyService;

    @InjectMocks
    private TypeService classUnderTest;

    @Before
    public void setUp() {
        initMocks(this);

        this.classUnderTest = new TypeService(searchService, dependencyService);
    }

    @Test(expected = DatapiException.class)
    public void convertToTypeDeclaration_null() throws Exception {
        this.classUnderTest.convertToTypeDeclaration(null);
    }

    @Test(expected = DatapiException.class)
    public void convertToTypeDeclaration_empty() throws Exception {
        this.classUnderTest.convertToTypeDeclaration("");
    }

    @Test(expected = DatapiException.class)
    public void convertToTypeDeclaration_malformed_type() throws Exception {
        this.classUnderTest.convertToTypeDeclaration("Map<List<String>>, <Integer>>");
    }

    @Test
    public void convertToTypeDeclaration() throws Exception {
        when(this.dependencyService.fetchDependency("Something")).thenReturn(new Dependency("somethingDep"));
        when(this.dependencyService.fetchDependency("Else")).thenReturn(new Dependency("elseDep"));
        when(this.searchService.findClassesWithName("Something")).thenReturn(Lists.newArrayList("Something"));
        when(this.searchService.findClassesWithName("Else")).thenReturn(Lists.newArrayList("Else"));

        TypeDeclaration declaration = this.classUnderTest.convertToTypeDeclaration("something  <Else<String>, int>");

        assertThat(declaration.getDeclaration(), equalTo("Something<Else<String>, Integer>"));

        List<Type> types = declaration.getTypes();
        assertThat(types, hasSize(4));

        List<String> typeNames = types.stream().map(Type::getClassName).collect(Collectors.toList());
        assertThat(typeNames, hasItems("Something", "Else", "String", "Integer"));

        List<String> dependencyNames = types.stream().map(Type::getDependency).filter(Objects::nonNull).map(Dependency::getPath).collect(Collectors.toList());
        assertThat(dependencyNames, hasSize(2));
        assertThat(dependencyNames, hasItems("somethingDep", "elseDep"));
    }

    @Test(expected = DatapiException.class)
    public void convertToType_malformed_type() throws Exception {
        this.classUnderTest.convertToType("Map<<string>", true);
    }

    @Test
    public void convertToType_should_be_primitive() throws Exception {
        Type foundType = this.classUnderTest.convertToType("int", true);

        assertThat(foundType.getClassName(), equalTo("int"));
    }

    @Test
    public void convertToType_should_not_be_primitive() throws Exception {
        Type foundType = this.classUnderTest.convertToType("int", false);

        assertThat(foundType.getClassName(), equalTo("Integer"));
    }

    @Test(expected = DatapiException.class)
    public void convertToType_custom_type_too_many_results() throws Exception {
        String className = "CustomClass";
        List<String> expectedResult = Lists.newArrayList("1", "2");

        when(this.searchService.findClassesWithName(className)).thenReturn(expectedResult);

        this.classUnderTest.convertToType(className, true);
    }

    @Test(expected = DatapiException.class)
    public void convertToType_custom_type_no_results() throws Exception {
        String className = "CustomClass";
        List<String> expectedResult = Lists.newArrayList();

        when(this.searchService.findClassesWithName(className)).thenReturn(expectedResult);

        this.classUnderTest.convertToType(className, true);
    }

    @Test
    public void convertToType_custom_type() throws Exception {
        String className = "CustomClass";
        String filePath = "src/main/java/com/example/CustomClass.java";

        List<String> expectedSearchResult = Lists.newArrayList(filePath);
        Dependency dependency = new Dependency("com.example.Something");

        when(this.searchService.findClassesWithName(className)).thenReturn(expectedSearchResult);
        when(this.dependencyService.fetchDependency(className)).thenReturn(dependency);

        Type type = this.classUnderTest.convertToType(className, true);

        assertThat(type.getClassName(), equalTo(className));
        assertThat(type.getDependency(), sameInstance(dependency));
    }

    /*
        VALIDATOR
     */
    @Test
    public void validateType_one_class() throws Exception {
        assertTrue(this.classUnderTest.validateType("Map"));
    }

    @Test
    public void validateType_single_type_arg() throws Exception {
        assertTrue(this.classUnderTest.validateType("Map<String>"));
    }

    @Test
    public void validateType_multiple_type_args() throws Exception {
        assertTrue(this.classUnderTest.validateType("Map<String, Integer>"));
    }

    @Test
    public void validateType_too_many_less_thans() {
        assertFalse(this.classUnderTest.validateType("Map<<String, Integer>"));
    }

    @Test
    public void validateType_missing_less_than() {
        assertFalse(this.classUnderTest.validateType("MapString, Integer>"));
    }

    @Test
    public void validateType_too_many_greater_thans() {
        assertFalse(this.classUnderTest.validateType("Map<String, Integer>>"));
    }

    @Test
    public void validateType_missing_greater_than() {
        assertFalse(this.classUnderTest.validateType("Map<String, Integer"));
    }

    @Test
    public void validateType_wrongly_bracketed() {
        assertFalse(this.classUnderTest.validateType("Map<<String, Integer>>"));
    }

    @Test
    public void validateType_primitive_in_type() {
        assertTrue(this.classUnderTest.validateType("List<boolean>"));
    }


}