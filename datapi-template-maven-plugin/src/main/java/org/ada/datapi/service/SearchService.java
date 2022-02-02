package org.ada.datapi.service;

import org.ada.datapi.data.Setting;
import org.ada.datapi.util.PathUtil;
import lombok.AllArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SearchService {

    private final ConfigurationService configurationService;

    /**
     * Finds the system path for classes with the given name, ignoring case
     *
     * @param className
     *          class name to search for
     * @return the java paths for all found class names
     */
    public List<String> findClassesWithName(String className) {
        String projectPath = this.configurationService.get(Setting.PROJECT_PATH);
        String basePath = this.configurationService.get(Setting.BASE_PATH);
        String appPath = this.configurationService.get(Setting.APP_PATH);

        String startSearchPath = PathUtil.joinPath(projectPath, basePath, appPath);

        return this.findFilesWithName(new File(startSearchPath), className + ".java").stream()
                .map(File::getPath)
                .map(p -> p.split(basePath)[1])
                .map(p -> PathUtil.normalize(p, "/"))
                .collect(Collectors.toList());
    }

    /**
     * Finds all file recursively from the root file given with the given search name.
     *
     * @param root
     *          place to start search
     * @param searchName
     *          name of file to look for
     * @return all files with the given name
     */
    private List<File> findFilesWithName(File root, String searchName) {
        List<File> foundFiles = new ArrayList<>();

        File[] files = root.listFiles();
        if (files == null) {
            return foundFiles;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().equalsIgnoreCase(searchName)) {
                foundFiles.add(file);
            } else if (file.isDirectory()) {
                foundFiles.addAll(findFilesWithName(file, searchName));
            }
        }

        return foundFiles;
    }

}
