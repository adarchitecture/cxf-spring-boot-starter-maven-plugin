package org.ada.datapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Field implements DatapiTemplate {

    private TypeDeclaration typeDeclaration;

    private String variableName;

    @Override
    public String getTemplate() {
        return String.format("\tprivate %s %s;", this.typeDeclaration.getTemplate(), this.variableName);
    }

}
