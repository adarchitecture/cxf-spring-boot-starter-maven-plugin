package org.ada.datapi.service.task.generate;

import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.exception.DatapiException;
import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.UtilityService;
import org.ada.datapi.service.task.DatapiTask;
import org.ada.datapi.service.task.DatapiTaskId;
import org.ada.datapi.service.template.TemplateService;
import lombok.Setter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Defines a common method of execution for tasks which should generate new files
 */
public abstract class GenerateTask extends DatapiTask {

    @Setter
    private static UtilityService utilityService = ServiceProvider.getUtilityService();

    final DatapiTask nextGeneration;

    final TemplateService templateService;

    /**
     *   @param errorMsg
     *          message to display on a failure
     * @param taskId
     *          id of this task
     * @param templateService
     * @param nextGeneration
     */
    GenerateTask(
            String errorMsg,
            DatapiTaskId taskId,
            TemplateService templateService,
            DatapiTask nextGeneration) {

        super(errorMsg, taskId);

        this.templateService = templateService;
        this.nextGeneration = nextGeneration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean execute(FileInformation fileInformation) {
        if (!utilityService.fileExists(fileInformation, this.templateService.getTemplateType())) {
            this.generate(fileInformation, this.templateService);
        } else {
            String infoMessage = String.format(
                    "%s already exists for scope ['%s'] - skipping generation",
                    fileInformation.getClassName(),
                    this.taskId.name());

            System.out.println(infoMessage);
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DatapiTask success() {
        return this.nextGeneration;
    }

    private void generate(FileInformation fileInformation, TemplateService templateService) {
        String genFilePath = utilityService.getFilePath(fileInformation, templateService.getTemplateType());
        String renderedFileString = templateService.renderTemplate(fileInformation);

        try {
            this.getOutputStream(genFilePath).write(renderedFileString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private OutputStream getOutputStream(String location) {
        File file = new File(location);
        if (file.exists()) {
            throw new DatapiException("Cannot generate. Already exists: " + location);
        }
        try {
            File dir = file.getParentFile();
            if ((dir.exists() || dir.mkdirs()) && file.createNewFile()) {
                return new FileOutputStream(new File(location));
            } else {
                throw new DatapiException("Error creating file. Ensure you have permissions to perform this action: " + location);
            }
        } catch (IOException e) {
            throw new DatapiException("Error creating file. Ensure you have permissions to perform this action: " + location);
        }
    }
}
