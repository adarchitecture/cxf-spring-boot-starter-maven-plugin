package org.ada.datapi.service.task.generate;

import org.ada.datapi.data.Setting;
import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.task.DatapiTaskId;
import org.ada.datapi.service.task.util.CheckSetting;
import org.ada.datapi.service.task.util.DoNothing;

public class ControllerGenerate extends GenerateTask {

    ControllerGenerate() {
        super(
                "Could not generate controller",
                DatapiTaskId.CONTROLLER,
                ServiceProvider.getControllerTemplateService(),
                new CheckSetting(
                        Setting.IS_TESTING_ENABLED,
                        new ControllerTestGenerate(),
                        new DoNothing()
                )
        );
    }

}
