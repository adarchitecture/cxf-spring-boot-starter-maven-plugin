package org.ada.datapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.ada.datapi.data.Setting;
import org.ada.datapi.domain.customization.JSONMap;
import org.ada.datapi.exception.DeveloperException;
import org.ada.datapi.exception.DatapiException;
import org.ada.datapi.util.PathUtil;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class ConfigurationService {

    private Map<Setting, String> configMap = new HashMap<>();

    private String applicationRoot;

    /**
     * This should not be used anywhere but when we are parsing the command
     * line arguments in {@link org.ada.datapi.Datapi}
     *
     * @param setting
     *          setting to change
     * @param value
     *
     */
    public void put(Setting setting, String value) {
        this.configMap.put(setting, value);
    }

    public String get(Setting setting) {
        return this.configMap.get(setting);
    }

    public boolean getBoolean(Setting setting) {
        return Boolean.valueOf(this.configMap.get(setting));
    }

    public ConfigurationService load(String applicationRoot) {
        try {
            this.loadDefaultSettings(applicationRoot);
            this.loadFromPom();
            this.loadFromSettingsFile();
        } catch (DatapiException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return this;
    }

    /**
     * Here we set all the settings to some default values
     * so that we never have an instance where a setting
     * key does not have a corresponding value. The defaults
     * as follows:
     *
     *  KEY                     = id:int
     *  IS_LOMBOK_ENABLED       = false
     *  PROJECT_PATH            =
     *  SUB_DIR_PATH            = *
     *  REL_PATH                = **
     *  MAVEN_GROUP_ID          = **
     *  REL_MODEL_PACKAGE       = model
     *  REL_REPOSITORY_PACKAGE  = repository
     *  REL_SERVICE_PACKAGE     = service
     *  REL_CONTROLLER_PACKAGE  = controller
     *  SHOULD_USE_PRIMITIVES   = false
     *  IS_TESTING_ENABLED      = true
     *
     * *  = This can only be configured at invocation as a command-line argument
     *
     * ** = These are not set, but will be set during inspection of the pom. If the
     *      pom cannot be read, the generation will fail, therefore, they do not
     *      need to be set in here.
     */
    private void loadDefaultSettings(String applicationRoot) {
        this.applicationRoot = applicationRoot;

        Setting.getDefaultedSettings()
                .forEach(s -> this.configMap.put(s, s.defaultVal));
    }

    /**
     * This loads all that information we can gather from the pom.xml file
     * into our settings configuration. These are the settings we can fetch
     * by reading information in the pom
     *
     *  MAVEN_GROUP_ID                          = found in pom under the group-id tag
     *  BASE_PATH                               = "src/main/java" in most (nearly all) cases
     *  APP_PATH                                = if group id == "com.example.app" app_path = "com/example/app/
     *  IS_LOMBOK_ENABLED                       = true if found in dependencies
     *  IS_REST_DOCUMENTATION_TESTING_ENABLED   = true if found in dependencies
     *
     */
    private void loadFromPom() {
        try {
            File pom = new File(FilenameUtils.concat(this.applicationRoot, "pom.xml"));
            if (!pom.exists()) {
                throw new DatapiException("Cannot find pom.xml, are you sure you are in the root of a project?");
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(pom);

            doc.getDocumentElement().normalize();

            NodeList groupIds = doc.getElementsByTagName("groupId");
            String mavenGroupId = groupIds.item(0).getTextContent();

            this.configMap.put(Setting.MAVEN_GROUP_ID, mavenGroupId);
            this.configMap.put(Setting.APP_PATH, PathUtil.
                    normalize(mavenGroupId.replace(".", "/"), "/"));

            String artifactName = doc.getElementsByTagName("artifactId").item(0).getTextContent();
            this.configMap.put(Setting.APP_NAME, artifactName);

            /*
                This part inspects the dependencies to see what we can turn on
             */
            List<Setting> dependencySettings = Setting.getDependentSettings();
            NodeList dependencies = doc.getElementsByTagName("dependency");
            for (int i=0; i < dependencies.getLength(); i++) {
                Element curDependency = (Element) dependencies.item(i);

                // Since we are in a dependency, there should only ever be one
                NodeList curGroupIds = curDependency.getElementsByTagName("groupId");
                if (curGroupIds.getLength() > 0) {

                    // Search for lombok
                    String dependencyId = curGroupIds.item(0).getTextContent();
                    for (Setting setting : dependencySettings) {
                        for (String dependencyStr : setting.dependencyFlags) {
                            if (dependencyId.contains(dependencyStr)) {
                                this.configMap.put(setting, "true");
                            }
                        }
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new DatapiException("Could not properly parse pom.xml");
        }

    }

    /**
     * Loads all the custom settings found in the plaster.yml file in the root of the project.
     */
    private void loadFromSettingsFile() {
        File plasterYaml = new File(FilenameUtils.concat(this.applicationRoot, "plaster.yml"));
        if (!plasterYaml.exists()) {
            return;
        }

        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        try {
            JSONMap customization = yamlMapper.readValue(plasterYaml, JSONMap.class);

            Setting.getConfigurableSettings()
                    .forEach((setting -> loadSetting(setting, customization)));
        } catch (IOException e) {
            // This just means the file is empty and isn't really an error
            // Todo: make this more reliable
            if (!e.getMessage().contains("No content to map due to end-of-input")) {
                throw new DatapiException("datapi.yml file is malformed", e);
            }
        }

    }

    /**
     * Gets the setting from the customization object passed in. If the setting is not
     * found in the map or is null (not acceptable), it will not overwrite what is already there.
     *
     * This also verifies that types are being properly passed. IE boolean fields are in fact
     * booleans, etc.
     *
     * @param setting
     *          setting to load
     * @param customizationMap
     *          object containing customizations
     */
    private void loadSetting(Setting setting, JSONMap customizationMap) {
        String foundObj = customizationMap.getEndValue(setting.compositePath);
        if (StringUtils.isNotEmpty(foundObj)) {
            if (verifyType(foundObj, setting.type)) {
                this.configMap.put(setting, foundObj);
            } else {
                throw new DatapiException("Could not parse " + foundObj + " to the required type " + setting.type.getName());
            }
        }
    }

    /**
     * Verifies that the passed in string is/can be the given type.
     *
     * @param obj
     *          the string to determine if it can be parsed into the given type
     * @param type
     *          the type to use to determine if the string is valid
     * @return whether or not the string passed can be parsed into the type
     */
    private boolean verifyType(String obj, Class<?> type) {
        if (type == String.class) {
            return true;
        } else if (type == Boolean.class) {
            return "true".equalsIgnoreCase(obj) || "false".equalsIgnoreCase(obj);
        }

        throw new DeveloperException();
    }

}
