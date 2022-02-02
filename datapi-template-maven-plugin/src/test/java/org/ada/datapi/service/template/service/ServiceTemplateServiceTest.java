package org.ada.datapi.service.template.service;

import com.google.common.collect.Lists;
import org.ada.datapi.data.Setting;
import org.ada.datapi.data.StoredJavaType;
import org.ada.datapi.domain.*;
import org.ada.datapi.domain.template.FlattenedField;
import org.ada.datapi.service.ConfigurationService;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ServiceTemplateServiceTest {

    @Mock
    private ConfigurationService configurationService;

    private ServiceTemplateService classUnderTest;

    private FileInformation fileInformation;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        this.classUnderTest = new ServiceTemplateService(configurationService);

        Field id = new Field(new TypeDeclaration("List", Lists.newArrayList(StoredJavaType.LIST.getType(false))), "id");
        Field mapField = new Field(new TypeDeclaration("Map", Lists.newArrayList(StoredJavaType.MAP.getType(false))), "var1");
        Field listField = new Field(new TypeDeclaration("List", Lists.newArrayList(StoredJavaType.LIST.getType(false))), "var2");
        Field exampleField = new Field(new TypeDeclaration("Example", Lists.newArrayList(new Type("Example", new Dependency("com.example.app.Example")))), "var3");

        this.fileInformation = new FileInformation("example_class", id, Lists.newArrayList(
                mapField, listField, exampleField
        ));
    }

    @Test
    public void getTemplate() {
        JtwigTemplate foundTemplate = this.classUnderTest.getTemplate();

        assertThat(foundTemplate, notNullValue());
    }

    @Test
    public void addCustomInformation() {
        JtwigModel model = JtwigModel.newModel();

        JtwigModel modifiedModel = this.classUnderTest.addCustomInformation(model, this.fileInformation);

        List<Dependency> dependencies = (List<Dependency>)modifiedModel.get("dependencies").get().getValue();

        assertThat(dependencies, hasSize(1));
        assertThat(modifiedModel.get("modelField").get().getValue(), equalTo(new FlattenedField("", "ExampleClass", "exampleClass")));
        assertThat(modifiedModel.get("serviceField").get().getValue(), equalTo(new FlattenedField("", "ExampleClassService", "exampleClassService")));
        assertThat(modifiedModel.get("repoField").get().getValue(), equalTo(new FlattenedField("", "ExampleClassRepository", "exampleClassRepository")));
        assertThat(modifiedModel.get("idField").get().getValue(), equalTo(new FlattenedField("", "List", "id")));
    }

    @Test
    public void renderTemplate() {
        String expected = "" +
                "package com.example.app.service.somewhere;\n" +
                "\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "\n" +
                "import java.util.List;\n" +
                "\n" +
                "import com.example.app.model.somewhere.ExampleClass;\n" +
                "import com.example.app.repository.somewhere.ExampleClassRepository;\n" +
                "\n" +
                "@Service\n" +
                "public class ExampleClassService {\n" +
                "\n" +
                "    private final ExampleClassRepository exampleClassRepository;\n" +
                "    \n" +
                "    @Autowired\n" +
                "    public ExampleClassService(ExampleClassRepository exampleClassRepository) {\n" +
                "        this.exampleClassRepository = exampleClassRepository;\n" +
                "    }\n" +
                "    \n" +
                "    public ExampleClass create(ExampleClass exampleClass) {\n" +
                "        return this.exampleClassRepository.save(exampleClass);\n" +
                "    }\n" +
                "    \n" +
                "    public ExampleClass read(List id) {\n" +
                "        return this.exampleClassRepository.findOne(id);\n" +
                "    }\n" +
                "    \n" +
                "    public ExampleClass update(ExampleClass exampleClass) {\n" +
                "        return this.exampleClassRepository.save(exampleClass);\n" +
                "    }\n" +
                "    \n" +
                "    public void delete(List id) {\n" +
                "        this.exampleClassRepository.delete(id);\n" +
                "    }\n" +
                "\n" +
                "}";

        when(this.configurationService.get(Setting.APP_PATH)).thenReturn("/com/example/app");
        when(this.configurationService.get(Setting.REL_MODEL_PACKAGE)).thenReturn("/model");
        when(this.configurationService.get(Setting.REL_REPOSITORY_PACKAGE)).thenReturn("/repository");
        when(this.configurationService.get(Setting.REL_CONTROLLER_PACKAGE)).thenReturn("/controller");
        when(this.configurationService.get(Setting.REL_SERVICE_PACKAGE)).thenReturn("/service");
        when(this.configurationService.get(Setting.SUB_DIR_PATH)).thenReturn("/somewhere");

        String actual = this.classUnderTest.renderTemplate(this.fileInformation);

        assertThat(expected, equalTo(actual));
    }
}