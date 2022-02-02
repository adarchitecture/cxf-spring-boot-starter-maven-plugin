package org.ada.datapi.service.template.model;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.service.ConfigurationService;
import org.ada.datapi.service.template.TemplateService;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class ModelTestTemplateService extends TemplateService {

    public ModelTestTemplateService(ConfigurationService configurationService) {
        super(TemplateType.MODEL_TEST, configurationService);
    }

    @Override
    protected JtwigModel addCustomInformation(JtwigModel model, FileInformation fileInformation) {
        return super.addTypeField(model, fileInformation.getClassName(), TemplateType.MODEL_TEST);
    }

    @Override
    protected JtwigTemplate getTemplate() {
        return JtwigTemplate.classpathTemplate("template/model/modelTest.twig");
    }
}
