package org.ada.datapi.it;

import com.google.common.collect.Lists;
import org.ada.datapi.Datapi;
import org.ada.datapi.data.Mode;
import org.ada.datapi.data.ModeScope;
import org.ada.datapi.data.Setting;
import org.ada.datapi.service.ConfigurationServiceTest;
import org.ada.datapi.service.ServiceProvider;
import lombok.ToString;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DatapiIt {

    private File modelFile;

    private File modelTestFile;

    private File repositoryFile;

    private File repositoryTestFile;

    private File serviceFile;

    private File serviceTestFile;

    private File controllerFile;

    private File controllerTestFile;

    private File controllerITFile;

    private List<File> allFiles;

    @Before
    public void setUp() {
        ConfigurationServiceTest.transformToItService(ServiceProvider.getConfigurationService());
    }

    private void ensureOnlyTheseFilesExist(List<File> filesThatShouldExist) {
        List<File> filesThatShouldNotExist = this.allFiles.stream()
                .filter(f -> !filesThatShouldExist.contains(f))
                .collect(Collectors.toList());

        for (File f : filesThatShouldExist) {
            assertTrue(f.getName() + " was expected to exist, but doesn't", f.exists());
        }

        for (File f : filesThatShouldNotExist) {
            assertFalse(f.getName() + " was expected to not exist, but does", f.exists());
        }
    }

    private void setFiles() {
        this.modelFile = new File("src/test/resources/testProject/src/main/java/com/example/app/somewhere/model/someplace/ExampleClass.java");
        this.modelTestFile = new File("src/test/resources/testProject/src/test/java/com/example/app/somewhere/model/someplace/ExampleClassTest.java");
        this.repositoryFile = new File("src/test/resources/testProject/src/main/java/com/example/app/somewhere/repository/someplace/ExampleClassRepository.java");
        this.repositoryTestFile = new File("src/test/resources/testProject/src/test/java/com/example/app/somewhere/repository/someplace/ExampleClassRepositoryTest.java");
        this.serviceFile = new File("src/test/resources/testProject/src/main/java/com/example/app/somewhere/service/someplace/ExampleClassService.java");
        this.serviceTestFile = new File("src/test/resources/testProject/src/test/java/com/example/app/somewhere/service/someplace/ExampleClassServiceTest.java");
        this.controllerFile = new File("src/test/resources/testProject/src/main/java/com/example/app/somewhere/controller/someplace/ExampleClassController.java");
        this.controllerTestFile = new File("src/test/resources/testProject/src/test/java/com/example/app/somewhere/controller/someplace/ExampleClassControllerTest.java");
        this.controllerITFile = new File("src/test/resources/testProject/src/test/java/com/example/app/somewhere/controller/someplace/ExampleClassControllerIT.java");

        this.allFiles = Lists.newArrayList(
                this.modelFile,
                this.modelTestFile,
                this.repositoryFile,
                this.repositoryTestFile,
                this.serviceFile,
                this.serviceTestFile,
                this.controllerFile,
                this.controllerTestFile,
                this.controllerITFile
        );
    }

    private void perform(Mode mode, ModeScope scope) {
        String[] args = new String[] {
                mode.name().toLowerCase(),
                scope.name().toLowerCase(),
                "-k key:string",
                "-d someplace",
                "example_class",
                "var1:map",
                "var2:List",
                "var3:example"
        };

        Datapi.main(args);
        this.setFiles();
    }

    @Test
    public void main() throws Exception {
        this.perform(Mode.GENERATE, ModeScope.SCAFFOLD);

        assertTrue(this.modelFile.exists());
        assertTrue(this.controllerFile.exists());
        assertTrue(this.serviceFile.exists());
        assertTrue(this.repositoryFile.exists());

        String modelContent = new String(Files.readAllBytes(this.modelFile.toPath()));
        String controllerContent = new String(Files.readAllBytes(this.controllerFile.toPath()));
        String serviceContent = new String(Files.readAllBytes(this.serviceFile.toPath()));
        String repositoryContent = new String(Files.readAllBytes(this.repositoryFile.toPath()));

        assertThat(GeneratedContent.MODEL_CLASS_LOMBOK_IT, equalTo(modelContent));
        assertThat(GeneratedContent.CONTROLLER_CLASS_IT, equalTo(controllerContent));
        assertThat(GeneratedContent.SERVICE_CLASS_IT, equalTo(serviceContent));
        assertThat(GeneratedContent.REPOSITORY_CLASS_IT, equalTo(repositoryContent));
    }

    @Test
    public void main_gen_scope_model() throws Exception {
        this.perform(Mode.GENERATE, ModeScope.MODEL);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList(
                this.modelFile, this.modelTestFile
        ));
    }

    @Test
    public void main_gen_scope_model_tests_not_enabled() throws Exception {
        ServiceProvider.getConfigurationService().put(Setting.IS_TESTING_ENABLED, "false");

        this.perform(Mode.GENERATE, ModeScope.MODEL);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList(
                this.modelFile
        ));
    }

    @Test
    public void main_gen_scope_repository() throws Exception {
        this.perform(Mode.GENERATE, ModeScope.REPOSITORY);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList(
                this.modelFile, this.modelTestFile,
                this.repositoryFile, this.repositoryTestFile
        ));
    }

    @Test
    public void main_gen_scope_repository_tests_not_enabled() throws Exception {
        ServiceProvider.getConfigurationService().put(Setting.IS_TESTING_ENABLED, "false");

        this.perform(Mode.GENERATE, ModeScope.REPOSITORY);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList(
                this.modelFile,
                this.repositoryFile
        ));
    }

    @Test
    public void main_gen_scope_service() throws Exception {
        this.perform(Mode.GENERATE, ModeScope.SERVICE);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList(
                this.modelFile, this.modelTestFile,
                this.repositoryFile, this.repositoryTestFile,
                this.serviceFile, this.serviceTestFile
        ));
    }

    @Test
    public void main_gen_scope_service_tests_not_enabled() throws Exception {
        ServiceProvider.getConfigurationService().put(Setting.IS_TESTING_ENABLED, "false");

        this.perform(Mode.GENERATE, ModeScope.SERVICE);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList(
                this.modelFile,
                this.repositoryFile,
                this.serviceFile
        ));
    }

    @Test
    public void main_del_scope_model_test_not_enabled() throws Exception {
        ServiceProvider.getConfigurationService().put(Setting.IS_TESTING_ENABLED, "false");

        this.perform(Mode.GENERATE, ModeScope.SCAFFOLD);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList(
                this.modelFile,
                this.repositoryFile,
                this.serviceFile,
                this.controllerFile
        ));

        this.perform(Mode.DELETE, ModeScope.MODEL);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList());
    }

    @Test
    public void main_del_scope_model() throws Exception {

        this.perform(Mode.GENERATE, ModeScope.SCAFFOLD);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList(
                this.modelFile, this.modelTestFile,
                this.repositoryFile, this.repositoryTestFile,
                this.serviceFile, this.serviceTestFile,
                this.controllerFile, this.controllerTestFile, this.controllerITFile
        ));

        this.perform(Mode.DELETE, ModeScope.MODEL);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList());
    }

    @Test
    public void main_del_scope_repository() throws Exception {
        this.perform(Mode.GENERATE, ModeScope.SCAFFOLD);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList(
                this.modelFile, this.modelTestFile,
                this.repositoryFile, this.repositoryTestFile,
                this.serviceFile, this.serviceTestFile,
                this.controllerFile, this.controllerTestFile, this.controllerITFile
        ));

        this.perform(Mode.DELETE, ModeScope.REPOSITORY);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList(
                this.modelFile, this.modelTestFile
        ));
    }

    @Test
    public void main_del_scope_service() throws Exception {
        this.perform(Mode.GENERATE, ModeScope.SCAFFOLD);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList(
                this.modelFile, this.modelTestFile,
                this.repositoryFile, this.repositoryTestFile,
                this.serviceFile, this.serviceTestFile,
                this.controllerFile, this.controllerTestFile, this.controllerITFile
        ));

        this.perform(Mode.DELETE, ModeScope.SERVICE);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList(
                this.modelFile, this.modelTestFile,
                this.repositoryFile, this.repositoryTestFile
        ));
    }

    @Test
    public void main_del_scope_controller() throws Exception {
        this.perform(Mode.GENERATE, ModeScope.SCAFFOLD);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList(
                this.modelFile, this.modelTestFile,
                this.repositoryFile, this.repositoryTestFile,
                this.serviceFile, this.serviceTestFile,
                this.controllerFile, this.controllerTestFile, this.controllerITFile
        ));

        this.perform(Mode.DELETE, ModeScope.CONTROLLER);

        this.ensureOnlyTheseFilesExist(Lists.newArrayList(
                this.modelFile, this.modelTestFile,
                this.repositoryFile, this.repositoryTestFile,
                this.serviceFile, this.serviceTestFile
        ));
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(new File("src/test/resources/testProject/src/main/java/com/example/app/somewhere/"));
        FileUtils.deleteDirectory(new File("src/test/resources/testProject/src/test/java/com/example/app/somewhere/"));
    }

}
