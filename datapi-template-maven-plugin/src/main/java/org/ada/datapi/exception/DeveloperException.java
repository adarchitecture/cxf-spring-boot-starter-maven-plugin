package org.ada.datapi.exception;

/**
 * This means something that a developer did ruined the program. There
 * are places in the application that require a programmer to type check
 * things. Such as what type a setting will be etc. When seen, it has the
 * connotation of "this line should never be hit".
 */
public class DeveloperException extends RuntimeException {

    private static final String msg = "A developer messed up. Please contact them, yell at them, and make them buy you things";

    public DeveloperException() {
        super(msg);
    }

}
