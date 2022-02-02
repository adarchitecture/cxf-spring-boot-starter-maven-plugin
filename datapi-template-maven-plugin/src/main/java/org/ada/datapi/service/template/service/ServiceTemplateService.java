package org.ada.datapi.service.template.service;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.service.ConfigurationService;
import org.ada.datapi.service.template.TemplateService;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class ServiceTemplateService extends TemplateService {

    public ServiceTemplateService(ConfigurationService configurationService) {
        super(TemplateType.SERVICE, configurationService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JtwigModel addCustomInformation(JtwigModel model, FileInformation fileInformation) {
        JtwigModel serviceModel;

        serviceModel = super.addTypeField(model, fileInformation.getClassName(), TemplateType.MODEL);
        serviceModel = super.addTypeField(serviceModel, fileInformation.getClassName(), TemplateType.SERVICE);
        serviceModel = super.addTypeField(serviceModel, fileInformation.getClassName(), TemplateType.REPOSITORY);
        serviceModel = super.addDependencies(serviceModel, fileInformation.getId());
        serviceModel = super.addId(serviceModel, fileInformation);

        return serviceModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JtwigTemplate getTemplate() {
        return JtwigTemplate.classpathTemplate("template/service/service.twig");
    }

}