package org.ada.datapi.service.task.delete;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.service.task.DatapiTaskId;

public class ModelDelete extends DeleteTask {

    public ModelDelete() {
        super(
                "Could not delete model",
                DatapiTaskId.MODEL,
                TemplateType.MODEL,
                new ModelTestDelete()
        );
    }

}

