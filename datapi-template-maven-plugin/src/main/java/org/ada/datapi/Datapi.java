package org.ada.datapi;

import org.ada.datapi.data.Arg;
import org.ada.datapi.data.Mode;
import org.ada.datapi.data.ModeScope;
import org.ada.datapi.data.Setting;
import org.ada.datapi.domain.Field;
import org.ada.datapi.domain.FileInformation;
import org.ada.datapi.exception.DeveloperException;
import org.ada.datapi.exception.DatapiException;
import org.ada.datapi.service.ConfigurationService;
import org.ada.datapi.service.FieldService;
import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.task.TaskService;
import org.ada.datapi.util.ArgParseUtil;
import lombok.experimental.UtilityClass;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Main entry point
 *
 * The way to interact with this application is through command line.
 *
 * plaster MODE MODE_SCOPE CLASS_NAME [FIELD:TYPE...]
 * plaster g scaffold Something name:string
 */
public class Datapi {

    private static ConfigurationService configurationService = ServiceProvider.getConfigurationService();

    private static FieldService fieldService = ServiceProvider.getFieldService();

    private static TaskService taskService = ServiceProvider.getTaskService();

    public static void main(String[] args) {
        ArgumentParser parser = ArgParseUtil.getArgParser();
        Namespace parsedArgs;
        try {
            parsedArgs = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            return;
        }

        try {
            ArgParseUtil.validateParsedArgs(parsedArgs);
            setCommandLineArgs(parsedArgs);

            FileInformation fileInformation = buildFileInformation(parsedArgs);
            Mode mode = Mode.getMode(parsedArgs.getString(Arg.MODE.key));
            String modeScope = parsedArgs.getString(Arg.MODE_SCOPE.key);

            taskService.perform(mode, ModeScope.valueOf(StringUtils.upperCase(modeScope)), fileInformation);
        } catch (DatapiException | DeveloperException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void setCommandLineArgs(Namespace parsedArgs) {
        /*
            Set the key if provided
         */
        String customKey = parsedArgs.getString(Arg.KEY.key);
        String keyString = StringUtils.isNotBlank(customKey) ? customKey : configurationService.get(Setting.KEY);

        configurationService.put(Setting.KEY, keyString);

        /*
            Set the custom directory if provided
         */
        String customSubDir = parsedArgs.get(Arg.DIR.key);
        String safeCustomSubDir = StringUtils.isBlank(customSubDir) ? "" : "/" + customSubDir;
        configurationService.put(Setting.SUB_DIR_PATH, safeCustomSubDir);
    }

    private static FileInformation buildFileInformation(Namespace parsedArgs) {
        String className = parsedArgs.getString(Arg.CLASS_NAME.key);
        Field key = fieldService.convertToField(configurationService.get(Setting.KEY));
        List<Field> fields = fieldService.convertToFields(parsedArgs.getList(Arg.FIELD.key));

        return new FileInformation(className, key, fields);
    }

}
