package org.ada.datapi.service.task.generate;

import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.task.DatapiTaskId;

public class ModelTestGenerate extends GenerateTask {

    public ModelTestGenerate() {
        super(
                "Could not generate model tests",
                DatapiTaskId.MODEL_TEST,
                ServiceProvider.getModelTestTemplateService(),
                new RepositoryGenerate()
        );
    }

}
