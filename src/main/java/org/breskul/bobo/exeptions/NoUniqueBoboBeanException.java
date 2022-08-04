package org.breskul.bobo.exeptions;

import static org.breskul.bobo.exeptions.ErrorMessages.NOT_UNIQUE_NAME;

public class NoUniqueBoboBeanException extends RuntimeException {
    public NoUniqueBoboBeanException(String message) {
        super(message + NOT_UNIQUE_NAME.getMessage());
    }
}
