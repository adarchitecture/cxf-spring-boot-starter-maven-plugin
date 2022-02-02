package org.ada.datapi.service.task.delete;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.service.task.DatapiTaskId;

public class ControllerITDelete extends DeleteTask {

    public ControllerITDelete() {
        super(
                "Could not delete controller integration test",
                DatapiTaskId.CONTROLLER_IT,
                TemplateType.CONTROLLER_IT,
                new ControllerTestDelete()
        );
    }

}
