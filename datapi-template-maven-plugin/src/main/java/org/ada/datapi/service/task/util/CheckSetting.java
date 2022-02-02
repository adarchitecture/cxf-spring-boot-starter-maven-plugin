package org.ada.datapi.service.task.util;

import org.ada.datapi.data.Setting;
import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.service.ConfigurationService;
import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.task.DatapiTask;

public class CheckSetting extends DatapiTask {

    private final ConfigurationService configurationService = ServiceProvider.getConfigurationService();

    private final Setting setting;

    private final DatapiTask trueTask;

    private final DatapiTask falseTask;

    public CheckSetting(Setting settingToCheck, DatapiTask taskIfEnabled, DatapiTask taskIfNotEnabled) {
        super("Setting: " + settingToCheck.name() + " is not of boolean type", null);

        this.setting = settingToCheck;
        this.trueTask = taskIfEnabled;
        this.falseTask = taskIfNotEnabled;
    }

    @Override
    protected boolean execute(FileInformation fileInformation) {
        return true;
    }

    @Override
    protected DatapiTask success() {
        return configurationService.getBoolean(this.setting) ? this.trueTask : this.falseTask;
    }
}
