package org.ada.datapi.service.task.generate;

import org.ada.datapi.data.Setting;
import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.task.DatapiTaskId;
import org.ada.datapi.service.task.util.CheckSetting;
import org.ada.datapi.service.task.util.DoNothing;

public class ControllerTestGenerate extends GenerateTask {

    public ControllerTestGenerate() {
        super(
                "Could not generate controller tests",
                DatapiTaskId.CONTROLLER_TEST,
                ServiceProvider.getControllerTestTemplateService(),
                new CheckSetting(
                        Setting.IS_TESTING_ENABLED,
                        new ControllerItGenerate(),
                        new DoNothing())
        );
    }

}
