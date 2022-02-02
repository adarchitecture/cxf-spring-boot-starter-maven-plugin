package org.ada.datapi.service.template.controller;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.service.ConfigurationService;
import org.ada.datapi.service.template.TemplateService;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class ControllerTestTemplateService extends TemplateService {

    public ControllerTestTemplateService(ConfigurationService configurationService) {
        super(TemplateType.CONTROLLER_TEST, configurationService);
    }

    @Override
    protected JtwigModel addCustomInformation(JtwigModel model, FileInformation fileInformation) {

        model = super.addId(model, fileInformation);
        model = super.addTypeField(model, fileInformation.getClassName(), TemplateType.CONTROLLER);
        model = super.addTypeField(model, fileInformation.getClassName(), TemplateType.CONTROLLER_TEST);
        model = super.addTypeField(model, fileInformation.getClassName(), TemplateType.MODEL);
        model = super.addTypeField(model, fileInformation.getClassName(), TemplateType.SERVICE);

        return model;
    }

    @Override
    protected JtwigTemplate getTemplate() {
        return JtwigTemplate.classpathTemplate("template/controller/controllerTest.twig");
    }

}
