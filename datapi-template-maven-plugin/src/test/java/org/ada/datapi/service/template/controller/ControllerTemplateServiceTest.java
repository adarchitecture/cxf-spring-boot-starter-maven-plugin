package org.ada.datapi.service.template.controller;

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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ControllerTemplateServiceTest {

    @Mock
    private ConfigurationService configurationService;

    private ControllerTemplateService classUnderTest;

    private FileInformation fileInformation;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        this.classUnderTest = new ControllerTemplateService(configurationService);

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

        assertThat(modifiedModel.get("dependencies").get().getValue(), equalTo(Lists.newArrayList(new Dependency("java.util.List"))));
        assertThat(modifiedModel.get("controllerField").get().getValue(), equalTo(new FlattenedField("", "ExampleClassController", "exampleClassController")));
        assertThat(modifiedModel.get("modelField").get().getValue(), equalTo(new FlattenedField("", "ExampleClass", "exampleClass")));
        assertThat(modifiedModel.get("serviceField").get().getValue(), equalTo(new FlattenedField("", "ExampleClassService", "exampleClassService")));
        assertThat(modifiedModel.get("idField").get().getValue(), equalTo(new FlattenedField("", "List", "id")));
        assertThat(modifiedModel.get("baseRoute").get().getValue(), equalTo("exampleClass"));
    }

    @Test
    public void renderTemplate() {
        String expected = "package com.example.app.controller.somewhere;\r\n\r\nimport org.springframework.beans.factory.annotation.Autowired;\r\nimport org.springframework.http.HttpStatus;\r\nimport org.springframework.web.bind.annotation.*;\r\n\r\nimport java.util.List;\n\nimport com.example.app.service.somewhere.ExampleClassService;\r\nimport com.example.app.model.somewhere.ExampleClass;\r\n\r\n@RestController\r\n@RequestMapping(\"/exampleClass\")\r\npublic class ExampleClassController {\r\n\r\n    private final ExampleClassService exampleClassService;\r\n\r\n    @Autowired\r\n    public ExampleClassController(ExampleClassService exampleClassService) {\r\n            this.exampleClassService = exampleClassService;\r\n    }\r\n\r\n    @RequestMapping(method = RequestMethod.POST)\r\n    @ResponseStatus(value = HttpStatus.CREATED)\r\n    public ExampleClass create(@RequestBody ExampleClass exampleClass) {\r\n            return this.exampleClassService.create(exampleClass);\r\n    }\r\n\r\n    @RequestMapping(value = \"/{id}\", method = RequestMethod.GET)\r\n    public ExampleClass read(@PathVariable List id) {\r\n            return this.exampleClassService.read(id);\r\n    }\r\n\r\n    @RequestMapping(value = \"/{id}\", method = RequestMethod.PUT)\r\n    public ExampleClass update(@PathVariable List id, @RequestBody ExampleClass exampleClass) {\r\n            return this.exampleClassService.update(exampleClass);\r\n    }\r\n\r\n    @RequestMapping(value = \"/{id}\", method = RequestMethod.DELETE)\r\n    public void delete(@PathVariable List id) {\r\n            this.exampleClassService.delete(id);\r\n    }\r\n\r\n}";

        when(this.configurationService.get(Setting.APP_PATH)).thenReturn("/com/example/app");
        when(this.configurationService.get(Setting.REL_MODEL_PACKAGE)).thenReturn("/model");
        when(this.configurationService.get(Setting.REL_CONTROLLER_PACKAGE)).thenReturn("/controller");
        when(this.configurationService.get(Setting.REL_SERVICE_PACKAGE)).thenReturn("/service");
        when(this.configurationService.get(Setting.SUB_DIR_PATH)).thenReturn("/somewhere");

        String actual = this.classUnderTest.renderTemplate(this.fileInformation);

        assertEquals(expected, equalTo(actual));
    }
}