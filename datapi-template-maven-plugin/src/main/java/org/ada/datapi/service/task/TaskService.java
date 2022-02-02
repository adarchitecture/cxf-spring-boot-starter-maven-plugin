package org.ada.datapi.service.task;

import org.ada.datapi.data.Mode;
import org.ada.datapi.data.ModeScope;
import org.ada.datapi.data.Setting;
import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.exception.DatapiException;
import org.ada.datapi.service.ConfigurationService;
import org.ada.datapi.service.task.delete.ControllerDelete;
import org.ada.datapi.service.task.generate.ModelGenerate;

import java.util.HashMap;
import java.util.Map;

public class TaskService {

    private final ConfigurationService configurationService;

    private final Map<ModeScope, DatapiTaskId> scopeDeleteTaskMap;

    public TaskService(ConfigurationService configurationService) {
        this.configurationService = configurationService;

        this.scopeDeleteTaskMap = new HashMap<>();
        this.scopeDeleteTaskMap.put(ModeScope.MODEL, DatapiTaskId.MODEL_TEST);
        this.scopeDeleteTaskMap.put(ModeScope.REPOSITORY, DatapiTaskId.REPOSITORY_TEST);
        this.scopeDeleteTaskMap.put(ModeScope.SERVICE, DatapiTaskId.SERVICE_TEST);
        this.scopeDeleteTaskMap.put(ModeScope.CONTROLLER, DatapiTaskId.CONTROLLER_TEST);
        this.scopeDeleteTaskMap.put(ModeScope.SCAFFOLD, DatapiTaskId.MODEL_TEST);
    }

    public void perform(Mode mode, ModeScope modeScope, FileInformation fileInformation) {
        DatapiTask initialTask = getInitialTask(mode);
        DatapiTaskId lastTaskId = getLastTaskId(mode, modeScope);

        this.perform(initialTask, fileInformation, lastTaskId);
    }

    private void perform(DatapiTask task, FileInformation fileInformation, DatapiTaskId lastTaskId) {
        if (task == null) {
            return;
        }

        if (task.execute(fileInformation)) {
            if (task.taskId == lastTaskId) {
                task.finish();
            } else {
                this.perform(task.success(), fileInformation, lastTaskId);
            }
        } else {
            task.failure();
        }
    }

    private DatapiTask getInitialTask(Mode mode) {
        switch(mode) {
            case GENERATE:
                return new ModelGenerate();
            case DELETE:
                return new ControllerDelete();
            default:
                return null;
        }
    }

    private DatapiTaskId getLastTaskId(Mode mode, ModeScope scope) {
        switch (mode) {
            case GENERATE:
                return this.getLastGenerationTaskId(scope);
            case DELETE:
                return this.scopeDeleteTaskMap.get(scope);
            default:
                return null;
        }
    }

    private DatapiTaskId getLastGenerationTaskId(ModeScope scope) {
        boolean testsEnabled = this.configurationService.getBoolean(Setting.IS_TESTING_ENABLED);

        switch (scope) {
            case MODEL:
                return (testsEnabled) ? DatapiTaskId.MODEL_TEST : DatapiTaskId.MODEL;
            case REPOSITORY:
                return (testsEnabled) ? DatapiTaskId.REPOSITORY_TEST : DatapiTaskId.REPOSITORY;
            case SERVICE:
                return (testsEnabled) ? DatapiTaskId.SERVICE_TEST : DatapiTaskId.SERVICE;
            case CONTROLLER:
            case SCAFFOLD:
                return (testsEnabled) ? DatapiTaskId.CONTROLLER_IT : DatapiTaskId.CONTROLLER;
            default:
                throw new DatapiException("Could not find end task for scope ['" + scope + "']");
        }
    }

}
