package org.ada.datapi.service.task.delete;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.service.task.DatapiTaskId;

public class ControllerTestDelete extends DeleteTask {

    public ControllerTestDelete() {
        super(
                "Could not delete controller tests",
                DatapiTaskId.CONTROLLER_TEST,
                TemplateType.CONTROLLER_TEST,
                new ServiceDelete()
        );
    }

}
