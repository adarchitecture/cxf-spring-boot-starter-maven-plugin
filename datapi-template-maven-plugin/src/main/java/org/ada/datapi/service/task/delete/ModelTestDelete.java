package org.ada.datapi.service.task.delete;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.service.task.DatapiTaskId;
import org.ada.datapi.service.task.util.DoNothing;

public class ModelTestDelete extends DeleteTask {

    public ModelTestDelete() {
        super(
                "Could not delete model tests",
                DatapiTaskId.MODEL_TEST,
                TemplateType.MODEL_TEST,
                new DoNothing()
        );
    }

}
