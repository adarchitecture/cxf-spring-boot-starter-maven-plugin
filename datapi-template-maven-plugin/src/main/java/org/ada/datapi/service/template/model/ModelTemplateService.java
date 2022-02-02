package org.ada.datapi.service.template.model;

import com.google.common.collect.Lists;
import org.ada.datapi.data.Setting;
import org.ada.datapi.data.TemplateType;
import org.ada.datapi.domain.Field;
import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.service.ConfigurationService;
import org.ada.datapi.service.template.TemplateService;
import org.ada.datapi.template.Template;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.util.List;

public class ModelTemplateService extends TemplateService {

    public ModelTemplateService(ConfigurationService configurationService) {
        super(TemplateType.MODEL, configurationService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JtwigModel addCustomInformation(JtwigModel model, FileInformation fileInformation) {
        JtwigModel modelModel;

        modelModel = super.addTypeField(model, fileInformation.getClassName(), TemplateType.MODEL);
        modelModel = super.addDependencies(modelModel, fileInformation);
        modelModel = super.addFields(modelModel, fileInformation);
        modelModel = super.addId(modelModel, fileInformation);

        if (this.configurationService.getBoolean(Setting.IS_LOMBOK_ENABLED)) {
            modelModel = addLombokHeader(modelModel);
        } else {
            modelModel = addGettersAndSetters(modelModel, fileInformation);
        }

        return modelModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JtwigTemplate getTemplate() {
        return JtwigTemplate.classpathTemplate("template/model/model.twig");
    }

    private JtwigModel addGettersAndSetters(JtwigModel model, FileInformation fileInformation) {
        StringBuilder methodString = new StringBuilder();
        List<Field> fields = Lists.newArrayList(fileInformation.getId());
        fields.addAll(fileInformation.getFields());

        for (Field field : fields) {
            String className = field.getTypeDeclaration().getDeclaration();
            String varName = field.getVariableName();

            methodString.append(Template.getGetterTemplate(className, varName));
            methodString.append("\n");
            methodString.append(Template.getSetterTemplate(className, varName));
            methodString.append("\n");
        }

        return model.with("methods", methodString.toString());
    }

    private JtwigModel addLombokHeader(JtwigModel model) {
        return model.with("header",
                "import lombok.AllArgsConstructor;\n" +
                "import lombok.Builder;\n" +
                "import lombok.Data;\n" +
                "import lombok.NoArgsConstructor;\n" +
                "\n" +
                "@AllArgsConstructor\n" +
                "@Builder\n" +
                "@Data\n" +
                "@NoArgsConstructor");
    }

}
