package org.ada.datapi.service.template.repository;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.service.ConfigurationService;
import org.ada.datapi.service.template.TemplateService;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class RepositoryTestTemplateService extends TemplateService {

    public RepositoryTestTemplateService(ConfigurationService configurationService) {
        super(TemplateType.REPOSITORY_TEST, configurationService);
    }

    @Override
    protected JtwigModel addCustomInformation(JtwigModel model, FileInformation fileInformation) {
        return super.addTypeField(model, fileInformation.getClassName(), TemplateType.REPOSITORY_TEST);
    }

    @Override
    protected JtwigTemplate getTemplate() {
        return JtwigTemplate.classpathTemplate("template/repository/repositoryTest.twig");
    }

}
