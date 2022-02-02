package org.ada.datapi.method;

import org.ada.datapi.service.type.ControllerModifier;
import org.ada.datapi.service.type.ModelModifier;
import org.ada.datapi.service.type.RepositoryModifier;
import org.ada.datapi.service.type.ServiceModifier;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Modify {

    public static final DatapiMethod MODEL = (DatapiMethod<ModelModifier>) ModelModifier::modifyModel;

    public static final DatapiMethod CONTROLLER = (DatapiMethod<ControllerModifier>) ControllerModifier::modifyController;

    public static final DatapiMethod SERVICE = (DatapiMethod<ServiceModifier>) ServiceModifier::modifyService;

    public static final DatapiMethod REPOSITORY = (DatapiMethod<RepositoryModifier>) RepositoryModifier::modifyRepository;

}
