package org.ada.datapi.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Dependency implements DatapiTemplate {

    public String path;

    public Dependency(String path) {
        this.path = path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplate() {
        return String.format("import %s;", this.path);
    }
}
