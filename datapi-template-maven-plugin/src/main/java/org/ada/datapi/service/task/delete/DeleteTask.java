package org.ada.datapi.service.task.delete;

import org.ada.datapi.data.TemplateType;
import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.exception.DatapiException;
import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.UtilityService;
import org.ada.datapi.service.task.DatapiTask;
import org.ada.datapi.service.task.DatapiTaskId;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

abstract class DeleteTask extends DatapiTask {

    @Setter
    private static UtilityService utilityService = ServiceProvider.getUtilityService();

    final DatapiTask nextGeneration;

    final TemplateType templateType;

    DeleteTask(String errorMsg, DatapiTaskId taskId, TemplateType templateType, DatapiTask nextGeneration) {
        super(errorMsg, taskId);

        this.templateType = templateType;
        this.nextGeneration = nextGeneration;
    }

    @Override
    protected boolean execute(FileInformation fileInformation) {
        if (utilityService.fileExists(fileInformation, this.templateType)) {
            this.delete(fileInformation, this.templateType);
        } else {
            String infoMessage = String.format(
                    "%s could not be found for scope ['%s'] - skipping deletion",
                    fileInformation.getClassName(),
                    this.taskId.name());

            System.out.println(infoMessage);
        }

        return true;
    }

    @Override
    protected DatapiTask success() {
        return this.nextGeneration;
    }

    private void delete(FileInformation fileInformation, TemplateType templateType) {
        String filePath = utilityService.getFilePath(fileInformation, templateType);

        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            throw new DatapiException("Error while deleting " + fileInformation + templateType.suffix);
        }
    }

}
