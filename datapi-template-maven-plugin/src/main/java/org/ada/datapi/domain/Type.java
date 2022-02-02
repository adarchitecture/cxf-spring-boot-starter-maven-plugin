package org.ada.datapi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * A type represents a single class usage in a file. It contains the class's name and
 * its dependency if it has one.
 *
 * Example:
 *
 *  Integer:null
 *  String:null
 *  Date:java.util.Date
 *
 * Non-examples:
 *
 *  List<String>
 *  Map<Integer, String>
 *
 *  For complex types see {@link TypeDeclaration}
 */
@Data
@EqualsAndHashCode
public class Type implements DatapiTemplate {

    private String className;

    private Dependency dependency;

    public Type(String type) {
        this(type, null);
    }

    public Type(@NonNull String className, Dependency dependency) {
        this.className = className.trim();
        this.dependency = dependency;
    }

    @Override
    public String getTemplate() {
        return this.className;
    }
}
