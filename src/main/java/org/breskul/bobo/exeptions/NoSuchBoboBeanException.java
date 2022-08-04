package org.breskul.bobo.exeptions;

import static org.breskul.bobo.exeptions.ErrorMessages.NOT_FOUND;

public class NoSuchBoboBeanException extends RuntimeException {
    public NoSuchBoboBeanException(String message) {
        super(message + NOT_FOUND.getMessage());
    }
}
