package org.ada.datapi.service.template.model;

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

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ModelTemplateServiceTest {

    @Mock
    private ConfigurationService configurationService;

    private ModelTemplateService classUnderTest;

    private FileInformation fileInformation;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        this.classUnderTest = new ModelTemplateService(configurationService);

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
    public void addCustomInformation_lombok_enabled() {
        JtwigModel model = JtwigModel.newModel();

        when(this.configurationService.getBoolean(Setting.IS_LOMBOK_ENABLED)).thenReturn(true);

        JtwigModel modifiedModel = this.classUnderTest.addCustomInformation(model, this.fileInformation);

        List<FlattenedField> addedFields = (List<FlattenedField>) modifiedModel.get("fields").get().getValue();
        List<Dependency> dependencies = (List<Dependency>)modifiedModel.get("dependencies").get().getValue();

        assertThat(dependencies, hasSize(3));
        assertThat(modifiedModel.get("modelField").get().getValue(), equalTo(new FlattenedField("", "ExampleClass", "exampleClass")));
        assertThat(modifiedModel.get("idField").get().getValue(), equalTo(new FlattenedField("", "List", "id")));
        assertTrue(modifiedModel.get("header").isPresent());
        assertFalse(modifiedModel.get("methods").isPresent());
        assertThat(addedFields, hasItems(
                new FlattenedField("", "Map", "var1", "null"),
                new FlattenedField("", "List", "var2", "null"),
                new FlattenedField("", "Example", "var3", "null")
        ));
    }

    @Test
    public void addCustomInformation_lombok_not_enabled() {
        JtwigModel model = JtwigModel.newModel();

        JtwigModel modifiedModel = this.classUnderTest.addCustomInformation(model, this.fileInformation);

        List<FlattenedField> addedFields = (List<FlattenedField>) modifiedModel.get("fields").get().getValue();
        List<Dependency> dependencies = (List<Dependency>)modifiedModel.get("dependencies").get().getValue();

        assertThat(dependencies, hasSize(3));
        assertThat(modifiedModel.get("modelField").get().getValue(), equalTo(new FlattenedField("", "ExampleClass", "exampleClass")));
        assertThat(modifiedModel.get("idField").get().getValue(), equalTo(new FlattenedField("", "List", "id")));
        assertFalse(modifiedModel.get("header").isPresent());
        assertTrue(modifiedModel.get("methods").isPresent());
        assertThat(addedFields, hasItems(
                new FlattenedField("", "Map", "var1", "null"),
                new FlattenedField("", "List", "var2", "null"),
                new FlattenedField("", "Example", "var3", "null")
        ));
    }

    @Test
    public void renderTemplate_lombok_enabled() throws Exception {
        String expected = "" +
                "package com.example.app.model.somewhere;\n" +
                "\n" +
                "import javax.persistence.*;\n" +
                "\n" +
                "import java.util.Map;\n" +
                "import java.util.List;\n" +
                "import com.example.app.Example;\n" +
                "\n" +
                "import lombok.AllArgsConstructor;\n" +
                "import lombok.Builder;\n" +
                "import lombok.Data;\n" +
                "import lombok.NoArgsConstructor;\n" +
                "\n" +
                "@AllArgsConstructor\n" +
                "@Builder\n" +
                "@Data\n" +
                "@NoArgsConstructor\n" +
                "@Entity\n" +
                "public class ExampleClass {\n" +
                "\n" +
                "    @Id\n" +
                "    @GeneratedValue(strategy = GenerationType.AUTO)\n" +
                "    private List id;\n" +
                "\n" +
                "    private Map var1;\n" +
                "\n" +
                "    private List var2;\n" +
                "\n" +
                "    private Example var3;\n" +
                "\n" +
                "}\n";

        when(this.configurationService.getBoolean(Setting.IS_LOMBOK_ENABLED)).thenReturn(true);
        when(this.configurationService.get(Setting.APP_PATH)).thenReturn("/com/example/app");
        when(this.configurationService.get(Setting.REL_MODEL_PACKAGE)).thenReturn("/model");
        when(this.configurationService.get(Setting.SUB_DIR_PATH)).thenReturn("/somewhere");

        String actual = this.classUnderTest.renderTemplate(this.fileInformation);

        assertThat(expected, equalTo(actual));
    }

    @Test
    public void renderTemplate_lombok_not_enabled() throws Exception {
        String expected = "" +
                "package com.example.app.model.somewhere;\n" +
                "\n" +
                "import javax.persistence.*;\n" +
                "\n" +
                "import java.util.Map;\n" +
                "import java.util.List;\n" +
                "import com.example.app.Example;\n" +
                "\n" +
                "@Entity\n" +
                "public class ExampleClass {\n" +
                "\n" +
                "    @Id\n" +
                "    @GeneratedValue(strategy = GenerationType.AUTO)\n" +
                "    private List id;\n" +
                "\n" +
                "    private Map var1;\n" +
                "\n" +
                "    private List var2;\n" +
                "\n" +
                "    private Example var3;\n" +
                "\n" +
                "    public List getId() {\n" +
                "        return this.id;\n" +
                "    }\n" +
                "\n" +
                "    public void setId(List id) {\n" +
                "        this.id = id;\n" +
                "    }\n" +
                "\n" +
                "    public Map getVar1() {\n" +
                "        return this.var1;\n" +
                "    }\n" +
                "\n" +
                "    public void setVar1(Map var1) {\n" +
                "        this.var1 = var1;\n" +
                "    }\n" +
                "\n" +
                "    public List getVar2() {\n" +
                "        return this.var2;\n" +
                "    }\n" +
                "\n" +
                "    public void setVar2(List var2) {\n" +
                "        this.var2 = var2;\n" +
                "    }\n" +
                "\n" +
                "    public Example getVar3() {\n" +
                "        return this.var3;\n" +
                "    }\n" +
                "\n" +
                "    public void setVar3(Example var3) {\n" +
                "        this.var3 = var3;\n" +
                "    }\n" +
                "\n" +
                "}\n";

        when(this.configurationService.get(Setting.APP_PATH)).thenReturn("/com/example/app");
        when(this.configurationService.get(Setting.REL_MODEL_PACKAGE)).thenReturn("/model");
        when(this.configurationService.get(Setting.SUB_DIR_PATH)).thenReturn("/somewhere");

        String actual = this.classUnderTest.renderTemplate(this.fileInformation);

        assertThat(expected, equalTo(actual));
    }

}