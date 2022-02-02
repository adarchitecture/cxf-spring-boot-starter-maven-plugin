package org.ada.datapi.service.task;

import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.exception.DatapiException;

public abstract class DatapiTask {

    private final String errorMsg;
    protected final DatapiTaskId taskId;

    public DatapiTask(String errorMsg, DatapiTaskId taskId) {
        this.errorMsg = errorMsg;
        this.taskId = taskId;
    }

    protected abstract boolean execute(FileInformation fileInformation);

    protected abstract DatapiTask success();

    /**
     * Throws an exception with the given text
     *
     * @throws DatapiException with the default message
     */
    protected void failure() {
        this.failure(this.errorMsg);
    }

    /**
     * Throws an exception with the given text
     *
     * @param message
     *          text to associate with the exception
     * @throws DatapiException with the corresponding message
     */
    protected void failure(String message) {
        throw new DatapiException(message);
    }

    protected void finish() {
        // Todo: wrap up things in the finish.
        // the main thought is to make all the generations be "pending"
        // generations until finished. This might have other purposes though too.
    }

}
