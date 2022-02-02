package org.ada.datapi.util;

import org.ada.datapi.data.Arg;
import org.ada.datapi.data.ModeScope;
import org.ada.datapi.exception.DatapiException;
import org.ada.datapi.service.FieldService;
import lombok.experimental.UtilityClass;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public final class ArgParseUtil {

    /**
     * Fetches the parser to be used to parse the command line arguments.
     *
     *      -d/--dir -  defines where to generate/look for a class. So if we are generating a
     *                  model, and provide "custom/dir", instead of generating in SETTING.REL_MODEL_PATH,
     *                  it will generate in SETTING.REL_MODEL_PATH/custom/dir. This is only active during
     *                  this instance of the run and is not persisted
     *
     *      -k/--key -  overwrites the way to generate the id of a model.
     *                  This overwrites the SETTING.KEY property for the current run
     *
     *      pos1     -  mode: Are we generating or removing? {@link org.ada.datapi.data.Mode}
     *
     *      pos2     -  modeScope: How much are we generating or removing? {@link ModeScope}
     *
     *      pos3     -  className: name of class we are focused on during this run
     *
     *      pos4     -  fields: list of name:type pairs we will be using to generate or remove.
     *
     * @return the argument parser
     */
    public static ArgumentParser getArgParser() {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("Datapi", true);

        /*
            Defines the mode argument
         */
        parser
                .addArgument(Arg.MODE.key)
                .choices("g", "gen", "generate", "d", "del", "delete", "m")
                .help("Whether to generate or remove code");

        /*
            Defines the scope of the mode argument
         */
        String[] modeScopes = Arrays.stream(ModeScope.values())
                .map(ModeScope::name)
                .map(String::toLowerCase)
                .toArray(String[]::new);
        parser
                .addArgument(Arg.MODE_SCOPE.key)
                .choices(modeScopes)
                .help("Determines what to generate/delete");

        /*
            Defines the class name argument
         */
        parser
                .addArgument(Arg.CLASS_NAME.key)
                .help("Name of class");

        /*
            Defines the field argument(s)
         */
        parser
                .addArgument(Arg.FIELD.key)
                .nargs("*")
                .help("name:type pair(s) defining fields");

        /*
            Define the custom sub-directory option
         */
        parser
                .addArgument("-d", "--" + Arg.DIR.key)
                .nargs("?")
                .help("sub directory in which to put/find generated classes");

        /*
            Define the custom id name:type pair option
         */
        parser
                .addArgument("-k", "--" + Arg.KEY.key)
                .nargs("?")
                .help("name:type pair indicating the name/type of the key for the class");

        return parser;
    }

    /**
     * Ensures that the parsed args are actually valid
     *
     * @param parsedArgs
     *          args that were parsed from the command line
     */
    public static void validateParsedArgs(Namespace parsedArgs) {
        String className = parsedArgs.getString(Arg.CLASS_NAME.key);
        if (!className.matches("[a-zA-Z_-]*")) {
            throw new DatapiException("Datapi doesn't support your weird class name. Use letters only");
        }

        List<String> fieldPairs = parsedArgs.getList(Arg.FIELD.key);
        for (String fieldPair : fieldPairs) {
            FieldService.validateFieldPair(fieldPair);
        }

        String customDir = parsedArgs.getString(Arg.DIR.key);
        if (StringUtils.isNotBlank(customDir)) {
            String normalizedPath = PathUtil.normalize(customDir, "/");
            if (!normalizedPath.matches("[a-zA-Z0-9/]+")) {
                throw new DatapiException("Invalid directory provided: " + customDir);
            }
        }

        String customKey = parsedArgs.getString(Arg.KEY.key);
        if (StringUtils.isNotBlank(customKey)) {
            FieldService.validateFieldPair(customKey);
        }
    }
}
