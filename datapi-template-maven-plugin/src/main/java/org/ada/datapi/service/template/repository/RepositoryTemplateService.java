package org.ada.datapi.service.template.repository;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.service.ConfigurationService;
import org.ada.datapi.service.template.TemplateService;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class RepositoryTemplateService extends TemplateService {

    public RepositoryTemplateService(ConfigurationService configurationService) {
        super(TemplateType.REPOSITORY, configurationService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JtwigModel addCustomInformation(JtwigModel model, FileInformation fileInformation) {
        JtwigModel repoModel;

        repoModel = super.addTypeField(model, fileInformation.getClassName(), TemplateType.REPOSITORY);
        repoModel = super.addTypeField(repoModel, fileInformation.getClassName(), TemplateType.MODEL);
        repoModel = super.addDependencies(repoModel, fileInformation.getId());
        repoModel = super.addId(repoModel, fileInformation);

        return repoModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JtwigTemplate getTemplate() {
        return JtwigTemplate.classpathTemplate("template/repository/repository.twig");
    }

}
