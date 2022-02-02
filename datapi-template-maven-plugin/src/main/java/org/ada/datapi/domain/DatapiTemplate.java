package org.ada.datapi.domain;

/**
 * Classes that contribute to generating the content in a file must implement this class.
 */
public interface DatapiTemplate {

    /**
     * Defines how an object should be represented in a file
     *
     * @return the string version of itself as represented in a Java file
     */
    String getTemplate();

}
