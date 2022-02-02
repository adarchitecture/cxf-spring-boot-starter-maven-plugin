package org.ada.datapi.service.task.delete;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.service.task.DatapiTaskId;

public class RepositoryDelete extends DeleteTask {

    RepositoryDelete() {
        super(
                "Could not delete repository",
                DatapiTaskId.REPOSITORY,
                TemplateType.REPOSITORY,
                new RepositoryTestDelete()
        );
    }

}
