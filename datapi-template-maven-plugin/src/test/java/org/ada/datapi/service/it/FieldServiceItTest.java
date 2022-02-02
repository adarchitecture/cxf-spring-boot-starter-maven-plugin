package org.ada.datapi.service.it;

import com.google.common.collect.Lists;
import org.ada.datapi.data.StoredJavaType;
import org.ada.datapi.domain.Dependency;
import org.ada.datapi.domain.Field;
import org.ada.datapi.domain.Type;
import org.ada.datapi.domain.TypeDeclaration;
import org.ada.datapi.service.ConfigurationServiceTest;
import org.ada.datapi.service.FieldService;
import org.ada.datapi.service.ServiceProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FieldServiceItTest {

    private FieldService classUnderTest = ServiceProvider.getFieldService();

    @Before
    public void setUp() {
        ConfigurationServiceTest.transformToItService(ServiceProvider.getConfigurationService());

        this.classUnderTest.setTypeService(ServiceProvider.getTypeService());
    }

    @Test
    public void convertToFields() {
        List<String> fieldPairs = Lists.newArrayList(
                "something:date",
                "some_other_thing:list<int>",
                "weird other thing: map<int, example>");

        List<Field> expected = Lists.newArrayList(
                new Field(new TypeDeclaration("Date",
                        Lists.newArrayList(
                                StoredJavaType.DATE.getType(false))),
                        "something"),
                new Field(new TypeDeclaration("List<Integer>",
                        Lists.newArrayList(
                                StoredJavaType.LIST.getType(false),
                                StoredJavaType.INTEGER.getType(false))),
                        "someOtherThing"),
            new Field(new TypeDeclaration("Map<Integer, Example>",
                Lists.newArrayList(
                        StoredJavaType.MAP.getType(false),
                        StoredJavaType.INTEGER.getType(false),
                        new Type("Example", new Dependency("com.example.app.dir1.Example")))),
                "weirdOtherThing")
        );

        List<Field> result = this.classUnderTest.convertToFields(fieldPairs);

        assertThat(result, equalTo(expected));
    }
}
