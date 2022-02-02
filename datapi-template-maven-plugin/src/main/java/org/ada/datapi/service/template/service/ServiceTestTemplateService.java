package org.ada.datapi.service.template.service;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.service.ConfigurationService;
import org.ada.datapi.service.template.TemplateService;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class ServiceTestTemplateService extends TemplateService {

    public ServiceTestTemplateService(ConfigurationService configurationService) {
        super(TemplateType.SERVICE_TEST, configurationService);
    }

    @Override
    protected JtwigModel addCustomInformation(JtwigModel model, FileInformation fileInformation) {

        model = super.addId(model, fileInformation);
        model = super.addTypeField(model, fileInformation.getClassName(), TemplateType.SERVICE);
        model = super.addTypeField(model, fileInformation.getClassName(), TemplateType.SERVICE_TEST);
        model = super.addTypeField(model, fileInformation.getClassName(), TemplateType.MODEL);
        model = super.addTypeField(model, fileInformation.getClassName(), TemplateType.REPOSITORY);

        return model;
    }

    @Override
    protected JtwigTemplate getTemplate() {
        return JtwigTemplate.classpathTemplate("template/service/serviceTest.twig");
    }
}
