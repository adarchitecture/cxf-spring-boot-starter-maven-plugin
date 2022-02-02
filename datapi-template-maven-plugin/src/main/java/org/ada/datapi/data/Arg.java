package org.ada.datapi.data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Arg {

    MODE("mode"),
    MODE_SCOPE("modeScope"),
    FIELD("field"),
    CLASS_NAME("className"),
    DIR("dir"),
    KEY("key");

    public String key;
}