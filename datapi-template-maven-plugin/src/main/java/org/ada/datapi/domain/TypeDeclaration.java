package org.ada.datapi.domain;

import org.ada.datapi.util.TypeUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

/**
 * This represents the type declaration when defining a variable.
 * For example:
 *
 *  private int var; ->                             TypeDeclaration represents "int"
 *  private Map<List<Integer>, SomeClass> map; ->   TypeDeclaration represents "Map<List<Integer>, SomeClass>"
 */
@Getter
@EqualsAndHashCode
public class TypeDeclaration implements DatapiTemplate {

    private final String declaration;

    private final List<Type> types;

    public TypeDeclaration(String declaration, List<Type> types) {
        this.declaration = TypeUtil.normalizeTypeString(declaration);
        this.types = types;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplate() {
        return this.declaration;
    }

}
