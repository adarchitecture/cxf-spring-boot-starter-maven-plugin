package org.ada.datapi.service.task.generate;

import org.ada.datapi.data.Setting;
import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.task.DatapiTaskId;
import org.ada.datapi.service.task.util.CheckSetting;

public class RepositoryGenerate extends GenerateTask {

    RepositoryGenerate() {
        super(
                "Could not generate repository",
                DatapiTaskId.REPOSITORY,
                ServiceProvider.getRepositoryTemplateService(),
                new CheckSetting(
                        Setting.IS_TESTING_ENABLED,
                        new RepositoryTestGenerate(),
                        new ServiceGenerate()
                )
        );
    }

}
