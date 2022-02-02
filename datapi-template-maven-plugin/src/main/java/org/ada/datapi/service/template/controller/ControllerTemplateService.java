package org.ada.datapi.service.template.controller;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.service.ConfigurationService;
import org.ada.datapi.service.template.TemplateService;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class ControllerTemplateService extends TemplateService {

    public ControllerTemplateService(ConfigurationService configurationService) {
        super(TemplateType.CONTROLLER, configurationService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JtwigModel addCustomInformation(JtwigModel model, FileInformation fileInformation) {
        JtwigModel controllerModel;

        controllerModel = super.addTypeField(model, fileInformation.getClassName(), TemplateType.CONTROLLER);
        controllerModel = super.addTypeField(controllerModel, fileInformation.getClassName(), TemplateType.MODEL);
        controllerModel = super.addTypeField(controllerModel, fileInformation.getClassName(), TemplateType.SERVICE);
        controllerModel = super.addDependencies(controllerModel, fileInformation.getId());
        controllerModel = super.addId(controllerModel, fileInformation);
        controllerModel = super.addBaseRoute(controllerModel, fileInformation);

        return controllerModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JtwigTemplate getTemplate() {
        return JtwigTemplate.classpathTemplate("template/controller/controller.twig");
    }

}