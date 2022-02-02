package org.ada.dev;

import com.github.zafarkhaja.semver.Version;
import com.google.common.collect.Lists;
import org.ada.datapi.util.ArgParseUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateVersions {

    private enum UpdateType {
        MAJOR(Version::incrementMajorVersion),
        MINOR(Version::incrementMinorVersion),
        PATCH(Version::incrementPatchVersion);

        VersionUpdater updater;
        UpdateType(VersionUpdater versionUpdater) {
            this.updater = versionUpdater;
        }
    }

    private static final String VERSION_REGEX = "\\d\\.\\d\\.\\d";

    public static void main(String[] args) throws IOException {
        UpdateType updateType;
        try {
            updateType = UpdateType.valueOf(StringUtils.upperCase(args[0]));
        } catch (Exception e) {
            System.out.println("Must provide an update type: ['major', 'minor', 'patch']");
            return;
        }

        updatePom(updateType);
        updateReadMe(updateType);
        updateCommandLineOptions();
    }

    private static void updatePom(UpdateType updateType) throws IOException {
        String before = "<version>";
        String after = "</version><!--PROJECT_ID-->";
        String filename = "pom.xml";

        updateSemVer(filename, before, after, updateType);
    }

    private static void updateReadMe(UpdateType updateType) throws IOException {
        String before = "\\[!\\[Version for JDrost1818/plaster]\\(https://img\\.shields\\.io/badge/semver-";
        String after = "-brightgreen\\.svg\\)]\\(\\)";
        String filename = "README.md";

        updateSemVer(filename, before, after, updateType);
    }

    private static void updateCommandLineOptions() throws IOException {
        File file = openFile("docs", "documentation", "command-line-usage.html");

        List<String> lines = Lists.newArrayList(
                "\n      $ " + StringUtils.substringBeforeLast(ArgParseUtil.getArgParser().formatUsage(), "\n"));

        FileUtils.writeLines(file, lines, "");
    }

    @SuppressWarnings("unchecked")
    private static void updateSemVer(String filename, String before, String after, UpdateType updateType) throws IOException {
        File file = openFile(filename);

        List<String> convertedLines = (List<String>) FileUtils.readLines(file).stream()
                .map(line -> {
                    String lineStr = line.toString();
                    if (lineStr.trim().matches(before + VERSION_REGEX + after)) {
                        Version version = Version.valueOf(
                                lineStr.trim()
                                        .replaceAll(before, "")
                                        .replaceAll(after, ""));

                        Version newVersion = updateType.updater.updateVersion(version);

                        return lineStr.replaceAll(VERSION_REGEX, newVersion.getNormalVersion());
                    }
                    return line;
                }).collect(Collectors.toList());

        FileUtils.writeLines(file, convertedLines);
    }

    private static File openFile(String... filename) {
        return Paths.get(System.getProperty("user.dir"), filename).toFile();
    }

    private interface VersionUpdater {

        Version updateVersion(Version versionToUpdate);

    }

}
