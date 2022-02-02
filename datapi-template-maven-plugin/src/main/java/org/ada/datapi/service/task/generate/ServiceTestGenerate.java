package org.ada.datapi.service.task.generate;

import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.task.DatapiTaskId;

public class ServiceTestGenerate extends GenerateTask {

    public ServiceTestGenerate() {
        super(
                "Could not generate service tests",
                DatapiTaskId.SERVICE_TEST,
                ServiceProvider.getServiceTestTemplateService(),
                new ControllerGenerate()
        );
    }

}
