package org.ada.datapi.service;

import com.google.common.collect.Lists;
import org.ada.datapi.domain.Field;
import org.ada.datapi.domain.TypeDeclaration;
import org.ada.datapi.exception.DatapiException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FieldServiceTest {

    @Mock
    private TypeService typeService;

    @InjectMocks
    private FieldService classUnderTest = ServiceProvider.getFieldService();

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test(expected = DatapiException.class)
    public void validateFieldPair_null() {
        FieldService.validateFieldPair(null);
    }

    @Test(expected = DatapiException.class)
    public void validateFieldPair_empty() {
        FieldService.validateFieldPair("");
    }

    @Test(expected = DatapiException.class)
    public void validateFieldPair_too_many_colons() {
        FieldService.validateFieldPair("too:many:colons");
    }

    @Test
    public void validateFieldPair() {
        FieldService.validateFieldPair("name:Type");
    }

    @Test(expected = DatapiException.class)
    public void convertToField_null() {
        this.classUnderTest.convertToField(null);
    }

    @Test(expected = DatapiException.class)
    public void convertToField_empty() {
        this.classUnderTest.convertToField("");
    }

    @Test
    public void convertToField() {
        String name = "name";
        String typeString = "string";
        String fieldPair = name + ":" + typeString;
        TypeDeclaration expectedTypeDeclaration = new TypeDeclaration("", Lists.newArrayList());

        when(this.typeService.convertToTypeDeclaration(typeString)).thenReturn(expectedTypeDeclaration);

        Field convertedField = this.classUnderTest.convertToField(fieldPair);

        assertThat(convertedField.getVariableName(), equalTo(name));
        assertThat(convertedField.getTypeDeclaration(), sameInstance(expectedTypeDeclaration));
    }

    @Test
    public void convertToFields_empty() {
        assertThat(this.classUnderTest.convertToFields(null), hasSize(0));
    }

    @Test(expected = DatapiException.class)
    public void convertToFields_invalids() {
        TypeDeclaration somethingTypeDeclaration = new TypeDeclaration("", Lists.newArrayList());
        TypeDeclaration elseTypeDeclaration = new TypeDeclaration("", Lists.newArrayList());

        when(this.typeService.convertToTypeDeclaration("something")).thenReturn(somethingTypeDeclaration);
        when(this.typeService.convertToTypeDeclaration("Else")).thenReturn(elseTypeDeclaration);

        this.classUnderTest.convertToFields(Lists.newArrayList("something:List", "Else:Integer", "error"));
    }

    @Test
    public void convertToFields() {
        TypeDeclaration somethingTypeDeclaration = new TypeDeclaration("", Lists.newArrayList());
        TypeDeclaration elseTypeDeclaration = new TypeDeclaration("", Lists.newArrayList());

        when(this.typeService.convertToTypeDeclaration("List")).thenReturn(somethingTypeDeclaration);
        when(this.typeService.convertToTypeDeclaration("Map<Integer, String>")).thenReturn(elseTypeDeclaration);

        List<Field> fields = this.classUnderTest.convertToFields(Lists.newArrayList("someVar:List", "otherVar:Map<Integer, String>"));
        List<Field> expectedFields = Lists.newArrayList(
                new Field(somethingTypeDeclaration, "someVar"),
                new Field(elseTypeDeclaration, "otherVar"));

        assertThat(fields, equalTo(expectedFields));
    }

}
