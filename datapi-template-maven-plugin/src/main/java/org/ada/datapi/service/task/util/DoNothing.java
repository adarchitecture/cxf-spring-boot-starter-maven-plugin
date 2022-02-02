package org.ada.datapi.service.task.util;

import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.service.task.DatapiTask;

public class DoNothing extends DatapiTask {

    public DoNothing() {
        super("", null);
    }

    @Override
    protected boolean execute(FileInformation fileInformation) {
        // The essence of this class is to do nothing, so... do nothing
        return true;
    }

    @Override
    protected DatapiTask success() {
        return null;
    }
}
