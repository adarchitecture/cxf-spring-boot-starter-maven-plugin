package org.ada.datapi.service;

import org.ada.datapi.data.Setting;
import org.ada.datapi.data.TemplateType;
import org.ada.datapi.domain.FileInformation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UtilityServiceTest {

    @Mock
    private ConfigurationService configurationService;

    @InjectMocks
    private UtilityService classUnderTest;

    @Before
    public void setUp() {
        initMocks(this);

        this.classUnderTest = new UtilityService(this.configurationService);

        when(this.configurationService.get(Setting.PROJECT_PATH)).thenReturn("project");
        when(this.configurationService.get(Setting.BASE_PATH)).thenReturn("base");
        when(this.configurationService.get(Setting.APP_PATH)).thenReturn("app");
        when(this.configurationService.get(Setting.SUB_DIR_PATH)).thenReturn("sub");
    }

    @Test
    public void getFilePath_model() throws Exception {
        when(this.configurationService.get(Setting.REL_MODEL_PACKAGE)).thenReturn("model");

        FileInformation fileInformation = new FileInformation("ClassName", null, null);

        String path = this.classUnderTest.getFilePath(fileInformation, TemplateType.MODEL);

        assertThat(path, equalTo("project/base/app/model/sub/ClassName.java"));
    }

    @Test
    public void getFilePath_controller() throws Exception {
        when(this.configurationService.get(Setting.REL_CONTROLLER_PACKAGE)).thenReturn("controller");

        FileInformation fileInformation = new FileInformation("ClassName", null, null);

        String path = this.classUnderTest.getFilePath(fileInformation, TemplateType.CONTROLLER);

        assertThat(path, equalTo("project/base/app/controller/sub/ClassNameController.java"));
    }

}