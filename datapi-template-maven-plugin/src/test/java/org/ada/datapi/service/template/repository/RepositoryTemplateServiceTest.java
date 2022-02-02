package org.ada.datapi.service.template.repository;

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

public class RepositoryTemplateServiceTest {

    @Mock
    private ConfigurationService configurationService;

    private RepositoryTemplateService classUnderTest;

    private FileInformation fileInformation;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        this.classUnderTest = new RepositoryTemplateService(configurationService);

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
        assertThat(modifiedModel.get("repoField").get().getValue(), equalTo(new FlattenedField("", "ExampleClassRepository", "exampleClassRepository")));
        assertThat(modifiedModel.get("idField").get().getValue(), equalTo(new FlattenedField("", "List", "id")));
    }

    @Test
    public void renderTemplate() {
        String expected = "" +
                "package com.example.app.repository.somewhere;\n" +
                "\n" +
                "import org.springframework.data.domain.Page;\n" +
                "import org.springframework.data.domain.Pageable;\n" +
                "import org.springframework.data.jpa.domain.Specification;\n" +
                "import org.springframework.data.repository.CrudRepository;\n" +
                "\n" +
                "import java.util.List;\n" +
                "\n" +
                "import com.example.app.model.somewhere.ExampleClass;\n" +
                "\n" +
                "public interface ExampleClassRepository extends CrudRepository<ExampleClass, List> {\n" +
                "\n" +
                "    Page<ExampleClass> findAll(Specification<ExampleClass> spec, Pageable pageInfo);\n" +
                "\n" +
                "    ExampleClass findOne(Specification<ExampleClass> spec);\n" +
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