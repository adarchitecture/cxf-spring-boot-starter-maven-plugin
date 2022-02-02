package org.ada.datapi.method;

import org.ada.datapi.domain.FileInformation;

public interface DatapiMethod<T> {

    void perform(T service, FileInformation information);

}
