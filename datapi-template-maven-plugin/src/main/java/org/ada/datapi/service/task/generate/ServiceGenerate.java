package org.ada.datapi.service.task.generate;

import org.ada.datapi.data.Setting;
import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.task.DatapiTaskId;
import org.ada.datapi.service.task.util.CheckSetting;

public class ServiceGenerate extends GenerateTask {

    ServiceGenerate() {
        super(
                "Could not generate service",
                DatapiTaskId.SERVICE,
                ServiceProvider.getServiceTemplateService(),
                new CheckSetting(
                        Setting.IS_TESTING_ENABLED,
                        new ServiceTestGenerate(),
                        new ControllerGenerate()
                )
        );
    }

}
