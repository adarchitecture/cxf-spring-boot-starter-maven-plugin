package org.ada.datapi.service;

import org.ada.datapi.data.Setting;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationServiceTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();


    public static ConfigurationService getTestConfigurationService(String root) {
        File file = new File(ConfigurationServiceTest.class.getClassLoader().getResource("testProject").getFile());
        return new ConfigurationService().load(file.getAbsolutePath() + "/" + root);
    }

    public static void transformToItService(ConfigurationService configurationService) {
        ConfigurationService itConfigurationService = getTestConfigurationService("");
        itConfigurationService.put(Setting.PROJECT_PATH, "src/test/resources/testProject/");

        for (Setting curSetting : Setting.values()) {
            configurationService.put(curSetting, itConfigurationService.get(curSetting));
        }
    }

    @Before
    public void setUp() throws Exception {
        
    }

    @Test
    public void load_pom_does_not_exist() throws Exception {
        this.exit.expectSystemExit();
        getTestConfigurationService("thisDoesNotExist.xml");
    }

    /**
     * Root1 only has a pom file with everything valid and enabled that can be enabled
     * through the pom inspection
     */
    @Test
    public void load_root1() throws Exception {
        ConfigurationService configurationService = getTestConfigurationService("/root1");

        assertThat(configurationService.get(Setting.KEY), equalTo("id:int"));
        assertThat(configurationService.get(Setting.IS_LOMBOK_ENABLED), equalTo("true"));
        assertThat(configurationService.get(Setting.PROJECT_PATH), equalTo(""));
        assertThat(configurationService.get(Setting.SUB_DIR_PATH), equalTo(""));
        assertThat(configurationService.get(Setting.BASE_PATH), equalTo("src/main/java"));
        assertThat(configurationService.get(Setting.APP_PATH), equalTo("com/example/app"));
        assertThat(configurationService.get(Setting.MAVEN_GROUP_ID), equalTo("com.example.app"));
        assertThat(configurationService.get(Setting.REL_MODEL_PACKAGE), equalTo("model"));
        assertThat(configurationService.get(Setting.REL_REPOSITORY_PACKAGE), equalTo("repository"));
        assertThat(configurationService.get(Setting.REL_SERVICE_PACKAGE), equalTo("service"));
        assertThat(configurationService.get(Setting.REL_CONTROLLER_PACKAGE), equalTo("controller"));
        assertThat(configurationService.get(Setting.SHOULD_USE_PRIMITIVES), equalTo("false"));
        assertThat(configurationService.get(Setting.IS_TESTING_ENABLED), equalTo("true"));
    }

    /**
     * Root2 has nothing enabled in the pom that isn't required, but
     * everything that can be configured in the plaster.yml file is configured
     */
    @Test
    public void load_root2() throws Exception {
        ConfigurationService configurationService = getTestConfigurationService("/root2");

        assertThat(configurationService.get(Setting.KEY), equalTo("key:string"));
        assertThat(configurationService.get(Setting.IS_LOMBOK_ENABLED), equalTo("true"));
        assertThat(configurationService.get(Setting.PROJECT_PATH), equalTo(""));
        assertThat(configurationService.get(Setting.SUB_DIR_PATH), equalTo(""));
        assertThat(configurationService.get(Setting.BASE_PATH), equalTo("custom/path"));
        assertThat(configurationService.get(Setting.APP_PATH), equalTo("com/example/app"));
        assertThat(configurationService.get(Setting.MAVEN_GROUP_ID), equalTo("com.example.app"));
        assertThat(configurationService.get(Setting.REL_MODEL_PACKAGE), equalTo("somewhere/model"));
        assertThat(configurationService.get(Setting.REL_REPOSITORY_PACKAGE), equalTo("somewhere/repository"));
        assertThat(configurationService.get(Setting.REL_SERVICE_PACKAGE), equalTo("somewhere/service"));
        assertThat(configurationService.get(Setting.REL_CONTROLLER_PACKAGE), equalTo("somewhere/controller"));
        assertThat(configurationService.get(Setting.SHOULD_USE_PRIMITIVES), equalTo("true"));
        assertThat(configurationService.get(Setting.IS_TESTING_ENABLED), equalTo("false"));
    }

    /**
     * Root3 has a pom file containing all required fields and an empty plaster.yml file
     */
    @Test
    public void load_root3() throws Exception {
        ConfigurationService configurationService = getTestConfigurationService("/root3");

        assertThat(configurationService.get(Setting.KEY), equalTo("id:int"));
        assertThat(configurationService.get(Setting.IS_LOMBOK_ENABLED), equalTo("false"));
        assertThat(configurationService.get(Setting.PROJECT_PATH), equalTo(""));
        assertThat(configurationService.get(Setting.SUB_DIR_PATH), equalTo(""));
        assertThat(configurationService.get(Setting.BASE_PATH), equalTo("src/main/java"));
        assertThat(configurationService.get(Setting.APP_PATH), equalTo("com/example/app"));
        assertThat(configurationService.get(Setting.MAVEN_GROUP_ID), equalTo("com.example.app"));
        assertThat(configurationService.get(Setting.REL_MODEL_PACKAGE), equalTo("model"));
        assertThat(configurationService.get(Setting.REL_REPOSITORY_PACKAGE), equalTo("repository"));
        assertThat(configurationService.get(Setting.REL_SERVICE_PACKAGE), equalTo("service"));
        assertThat(configurationService.get(Setting.REL_CONTROLLER_PACKAGE), equalTo("controller"));
        assertThat(configurationService.get(Setting.SHOULD_USE_PRIMITIVES), equalTo("false"));
        assertThat(configurationService.get(Setting.IS_TESTING_ENABLED), equalTo("true"));
    }

    /**
     * Root4 has an invalid type. A string where there should be a boolean
     */
    @Test
    public void load_root4() throws Exception {
        this.exit.expectSystemExit();
        getTestConfigurationService("/root4");
    }


}