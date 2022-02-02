package org.ada.datapi.service.task.delete;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.service.task.DatapiTaskId;

public class ServiceDelete extends DeleteTask {

    ServiceDelete() {
        super(
                "Could not delete service",
                DatapiTaskId.SERVICE,
                TemplateType.SERVICE,
                new ServiceTestDelete()
        );
    }

}
