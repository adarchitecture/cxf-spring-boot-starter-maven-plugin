package org.ada.datapi.service.task;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DatapiTaskId {

    MODEL(""),
    MODEL_TEST(""),
    REPOSITORY(""),
    REPOSITORY_TEST(""),
    SERVICE(""),
    SERVICE_TEST(""),
    SERVICE_IT(""),
    CONTROLLER(""),
    CONTROLLER_TEST(""),
    CONTROLLER_IT(""),
    CONTROLLER_DOC_IT(""),
    DOC_IT_PARENT("");

    public final String description;
}
