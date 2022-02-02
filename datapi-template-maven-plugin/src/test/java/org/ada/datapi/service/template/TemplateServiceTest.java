package org.ada.datapi.service.template;

import com.google.common.collect.Lists;
import org.ada.datapi.data.StoredJavaType;
import org.ada.datapi.data.TemplateType;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.MockitoAnnotations.initMocks;

public class TemplateServiceTest {

    @Mock
    private ConfigurationService configurationService;

    private TemplateService templateService;

    private FileInformation fileInformation;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        this.templateService = new TemplateServiceImpl(this.configurationService);

        Field id = new Field(new TypeDeclaration("List", Lists.newArrayList(StoredJavaType.LIST.getType(false))), "id");
        Field mapField = new Field(new TypeDeclaration("Map", Lists.newArrayList(StoredJavaType.MAP.getType(false))), "var1");
        Field listField = new Field(new TypeDeclaration("List", Lists.newArrayList(StoredJavaType.LIST.getType(false))), "var2");
        Field exampleField = new Field(new TypeDeclaration("Example", Lists.newArrayList(new Type("Example", new Dependency("com.example.app.Example")))), "var3");

        this.fileInformation = new FileInformation("example_class", id, Lists.newArrayList(
                mapField, listField, exampleField
        ));
    }

    @Test
    public void addFields() throws Exception {
        JtwigModel model = JtwigModel.newModel();

        JtwigModel modifiedModel = this.templateService.addFields(model, this.fileInformation);

        List<FlattenedField> addedFields = (List<FlattenedField>) modifiedModel.get("fields").get().getValue();

        assertThat(addedFields, hasItems(
                new FlattenedField("", "Map", "var1", "null"),
                new FlattenedField("", "List", "var2", "null"),
                new FlattenedField("", "Example", "var3", "null")
        ));
    }

    @Test
    public void addId() throws Exception {
        JtwigModel model = JtwigModel.newModel();

        JtwigModel modifiedModel = this.templateService.addId(model, this.fileInformation);

        FlattenedField idField = (FlattenedField) modifiedModel.get("idField").get().getValue();

        assertThat(idField, equalTo(new FlattenedField("", "List", "id")));
    }

    @Test
    public void addDependencies_no_dependencies() throws Exception {
        JtwigModel model = JtwigModel.newModel();

        Field field = new Field(new TypeDeclaration("Integer", Lists.newArrayList(StoredJavaType.INTEGER.getType(false))), "var1");

        List<Dependency> dependencies = (List<Dependency>) this.templateService.addDependencies(model, Lists.newArrayList(field)).get("dependencies").get().getValue();

        assertThat(dependencies, hasSize(0));
    }

    @Test
    public void addDependencies_multiple_same_dependencies() throws Exception {
        JtwigModel model = JtwigModel.newModel();

        Field field1 = new Field(new TypeDeclaration("Map", Lists.newArrayList(StoredJavaType.MAP.getType(false))), "var1");
        Field field2 = new Field(new TypeDeclaration("Map", Lists.newArrayList(StoredJavaType.MAP.getType(false))), "var2");

        List<Dependency> dependencies = (List<Dependency>) this.templateService.addDependencies(model, Lists.newArrayList(field1, field2)).get("dependencies").get().getValue();;

        assertThat(dependencies, hasSize(1));
        assertThat(dependencies.get(0), equalTo(new Dependency("java.util.Map")));
    }

    @Test
    public void addDependency() throws Exception {
        JtwigModel model = JtwigModel.newModel();

        Field field1 = new Field(new TypeDeclaration("Map", Lists.newArrayList(StoredJavaType.MAP.getType(false))), "var1");

        List<Dependency> dependencies = (List<Dependency>) this.templateService.addDependencies(model, field1).get("dependencies").get().getValue();

        assertThat(dependencies, hasSize(1));
        assertThat(dependencies.get(0), equalTo(new Dependency("java.util.Map")));
    }

    @Test
    public void addDependencies() throws Exception {
        JtwigModel model = JtwigModel.newModel();


        List<Dependency> dependencies = (List<Dependency>) this.templateService.addDependencies(model, fileInformation).get("dependencies").get().getValue();

        assertThat(dependencies, hasSize(3));
        assertThat(dependencies, hasItems(
                new Dependency("java.util.List"),
                new Dependency("java.util.Map"),
                new Dependency("com.example.app.Example")));
    }

    private class TemplateServiceImpl extends TemplateService {

        public TemplateServiceImpl(ConfigurationService configurationService) {
            super(TemplateType.SERVICE, configurationService);
        }

        @Override
        public JtwigModel addCustomInformation(JtwigModel model, FileInformation fileInformation) {
            return null;
        }

        @Override
        public JtwigTemplate getTemplate() {
            return null;
        }
    }

}