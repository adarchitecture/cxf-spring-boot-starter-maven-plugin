package org.ada.datapi.service.task.generate;

import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.task.DatapiTaskId;
import org.ada.datapi.service.task.util.DoNothing;

public class ControllerItGenerate extends GenerateTask {

    public ControllerItGenerate() {
        super(
                "Could not generate controller integration tests",
                DatapiTaskId.CONTROLLER_IT,
                ServiceProvider.getControllerItTemplateService(),
                new DoNothing()
        );
    }


}
