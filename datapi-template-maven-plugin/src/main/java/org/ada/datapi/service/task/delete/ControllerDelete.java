package org.ada.datapi.service.task.delete;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.service.task.DatapiTaskId;

public class ControllerDelete extends DeleteTask {

    public ControllerDelete() {
        super(
                "Could not delete controller",
                DatapiTaskId.CONTROLLER,
                TemplateType.CONTROLLER,
                new ControllerITDelete()
        );
    }

}
