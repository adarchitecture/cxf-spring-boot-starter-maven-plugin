package org.ada.datapi.service.task.generate;

import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.task.DatapiTaskId;

public class RepositoryTestGenerate extends GenerateTask {

    public RepositoryTestGenerate() {
        super(
                "Could not generate repository tests",
                DatapiTaskId.REPOSITORY_TEST,
                ServiceProvider.getRepositoryTestTemplateService(),
                new ServiceGenerate()
        );
    }

}
