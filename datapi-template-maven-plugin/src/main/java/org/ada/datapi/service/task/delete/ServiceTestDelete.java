package org.ada.datapi.service.task.delete;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.service.task.DatapiTaskId;

public class ServiceTestDelete extends DeleteTask {

    public ServiceTestDelete() {
        super(
                "Could not delete service tests",
                DatapiTaskId.SERVICE_TEST,
                TemplateType.SERVICE_TEST,
                new RepositoryDelete()
        );
    }

}
