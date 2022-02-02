package org.ada.datapi.service.task.util;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.UtilityService;
import org.ada.datapi.service.task.DatapiTask;

import static java.util.Objects.nonNull;

public class CheckFileExists extends DatapiTask {

    private final UtilityService utilityService = ServiceProvider.getUtilityService();

    private final TemplateType templateType;

    private final DatapiTask trueTask;

    private final DatapiTask falseTask;

    private FileInformation fileInformation;

    public CheckFileExists(TemplateType templateType, DatapiTask taskIfExists, DatapiTask taskIfDoesNotExist) {
        super("", null);

        this.templateType = templateType;
        this.trueTask = taskIfExists;
        this.falseTask = taskIfDoesNotExist;
    }

    @Override
    protected boolean execute(FileInformation fileInformation) {
        this.fileInformation = fileInformation;

        return true;
    }

    @Override
    protected DatapiTask success() {
        if (nonNull(this.fileInformation) && this.utilityService.fileExists(this.fileInformation, this.templateType)) {
            return trueTask;
        }
        return this.falseTask;
    }

}
