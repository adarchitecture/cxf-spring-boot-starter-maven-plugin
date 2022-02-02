package org.ada.datapi.service.task.delete;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.service.task.DatapiTaskId;

public class RepositoryTestDelete extends DeleteTask {

    public RepositoryTestDelete() {
        super(
                "Could not delete repository tests",
                DatapiTaskId.REPOSITORY_TEST,
                TemplateType.REPOSITORY_TEST,
                new ModelDelete()
        );
    }

}
