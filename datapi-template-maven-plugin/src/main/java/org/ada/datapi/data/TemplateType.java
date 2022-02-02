package org.ada.datapi.data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TemplateType {

    MODEL(
            "",
            "modelField",
            "model/model.twig",
            Setting.REL_MODEL_PACKAGE,
            GenerationLocation.CODE
    ),
    REPOSITORY(
            "Repository",
            "repoField",
            "repository/repository.twig",
            Setting.REL_REPOSITORY_PACKAGE,
            GenerationLocation.CODE
    ),
    SERVICE(
            "Service",
            "serviceField",
            "service/service.twig",
            Setting.REL_SERVICE_PACKAGE,
            GenerationLocation.CODE
    ),
    CONTROLLER(
            "Controller",
            "controllerField",
            "controller/controller.twig",
            Setting.REL_CONTROLLER_PACKAGE,
            GenerationLocation.CODE
    ),
    MODEL_TEST(
            "Test",
            "modelTestField",
            "model/modelTest.twig",
            Setting.REL_MODEL_PACKAGE,
            GenerationLocation.TEST
    ),
    REPOSITORY_TEST(
            "RepositoryTest",
            "repoTestField",
            "repo/repositoryTest.twig",
            Setting.REL_REPOSITORY_PACKAGE,
            GenerationLocation.TEST
    ),
    SERVICE_TEST(
            "ServiceTest",
            "serviceTestField",
            "service/serviceTest.twig",
            Setting.REL_SERVICE_PACKAGE,
            GenerationLocation.TEST
    ),
    SERVICE_IT(
            "ServiceIT",
            "serviceTestField",
            "service/serviceIT.twig",
            Setting.REL_SERVICE_PACKAGE,
            GenerationLocation.TEST
    ),
    CONTROLLER_TEST(
            "ControllerTest",
            "controllerTestField",
            "controller/controllerTest.twig",
            Setting.REL_CONTROLLER_PACKAGE,
            GenerationLocation.TEST
    ),
    CONTROLLER_IT(
            "ControllerIT",
            "controllerITField",
            "controller/controllerIT.twig",
            Setting.REL_CONTROLLER_PACKAGE,
            GenerationLocation.TEST
    );

    public final String suffix;
    public final String templateVarName;
    public final String templateLocation;
    public final Setting relPathSetting;
    public final GenerationLocation generationLocation;


}
