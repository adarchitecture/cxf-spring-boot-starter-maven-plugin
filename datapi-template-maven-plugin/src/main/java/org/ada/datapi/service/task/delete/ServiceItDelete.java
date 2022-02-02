package org.ada.datapi.service.task.delete;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.service.task.DatapiTaskId;

public class ServiceItDelete extends DeleteTask {

    public ServiceItDelete() {
        super(
                "Could not delete service integration tests",
                DatapiTaskId.SERVICE_IT,
                TemplateType.SERVICE_IT,
                new ServiceTestDelete()
        );
    }

}
