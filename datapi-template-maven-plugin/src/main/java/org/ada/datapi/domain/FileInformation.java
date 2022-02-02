package org.ada.datapi.domain;

import org.ada.datapi.util.TypeUtil;
import lombok.Data;

import java.util.List;

@Data
public class FileInformation {

    private String className;

    private Field id;

    private List<Field> fields;

    public FileInformation(String className, Field id, List<Field> fields) {
        this.className = TypeUtil.normalizeTypeString(className);
        this.id = id;
        this.fields = fields;
    }

}
