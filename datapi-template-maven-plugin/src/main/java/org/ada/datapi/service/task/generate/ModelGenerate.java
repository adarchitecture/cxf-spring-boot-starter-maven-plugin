package org.ada.datapi.service.task.generate;

import org.ada.datapi.data.Setting;
import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.task.DatapiTaskId;
import org.ada.datapi.service.task.util.CheckSetting;

public class ModelGenerate extends GenerateTask {

    public ModelGenerate() {
        super(
                "Could not generate model",
                DatapiTaskId.MODEL,
                ServiceProvider.getModelTemplateService(),
                new CheckSetting(
                        Setting.IS_TESTING_ENABLED,
                        new ModelTestGenerate(),
                        new RepositoryGenerate()
                )
        );
    }

}

