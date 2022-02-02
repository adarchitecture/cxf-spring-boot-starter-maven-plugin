package org.ada.datapi.service;

import java.nio.file.Paths;

import org.ada.datapi.data.GenerationLocation;
import org.ada.datapi.data.Setting;
import org.ada.datapi.data.TemplateType;
import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.util.PathUtil;
import org.ada.datapi.util.TypeUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UtilityService {

    private final ConfigurationService configurationService;

    public String getPackage(FileInformation fileInformation, TemplateType templateType) {
        String appPath = this.configurationService.get(Setting.APP_PATH);
        String dirPath = this.configurationService.get(templateType.relPathSetting);
        String customPath = this.configurationService.get(Setting.SUB_DIR_PATH);
        String fileName = fileInformation.getClassName() + templateType.suffix + ".java";

        if (templateType.generationLocation == GenerationLocation.ROOT_TEST) {
            String itFileName =  this.configurationService.get(Setting.APP_NAME)  + templateType.suffix;
            itFileName = TypeUtil.normalizeTypeString(itFileName) + ".java";

            return PathUtil.joinPath(appPath, itFileName);
        }

        return PathUtil.joinPath(appPath, dirPath, customPath, fileName);
    }

    public String getFilePath(FileInformation fileInformation, TemplateType templateType) {
        String projectPath = this.configurationService.get(Setting.PROJECT_PATH);
        String basePath = this.configurationService.get(Setting.BASE_PATH);
        String appPath = this.configurationService.get(Setting.APP_PATH);
        String dirPath = this.configurationService.get(templateType.relPathSetting);
        String customPath = this.configurationService.get(Setting.SUB_DIR_PATH);
        String fileName = fileInformation.getClassName() + templateType.suffix + ".java";

        // Todo - This is going to get out of hand. Fix it... Somehow
        if (templateType.generationLocation == GenerationLocation.TEST) {
            basePath = basePath.replace("src/main", "src/test");
        } else if (templateType.generationLocation == GenerationLocation.ROOT_TEST) {
            basePath = basePath.replace("src/main", "src/test");
            String itFileName =  this.configurationService.get(Setting.APP_NAME)  + templateType.suffix;
            itFileName = TypeUtil.normalizeTypeString(itFileName) + ".java";

            return PathUtil.joinPath(projectPath, basePath, appPath, itFileName);
        }

        return PathUtil.joinPath(projectPath, basePath, appPath, dirPath, customPath, fileName);
    }

    public boolean fileExists(FileInformation fileInformation, TemplateType templateType) {
        return Paths.get(getFilePath(fileInformation, templateType)).toFile().exists();
    }

}
