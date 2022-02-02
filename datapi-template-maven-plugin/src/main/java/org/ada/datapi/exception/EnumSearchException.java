package org.ada.datapi.exception;

/**
 * Exception to be thrown when searching in an enum and not finding any results
 */
public class EnumSearchException extends Exception {

    public EnumSearchException (String message) {
        super (message);
    }

}
