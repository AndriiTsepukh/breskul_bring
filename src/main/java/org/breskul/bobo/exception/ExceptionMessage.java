package org.breskul.bobo.exception;

public enum ExceptionMessage {
    ;
    public static final String BOBO_VALUE_EXCEPTION = "Unable to inject value for property {} annotated with @BoboValue:";
    public static final String READ_PROPERTY_FILE_EXCEPTION = "Exception occurred during reading properties from the following file: {}";
    public static final String GET_FILE_EXCEPTION = "Error occurred during parsing {} as an URI reference";
    public static final String PROPERTY_NOT_FOUND_EXCEPTION = "Could not find value for property %s";

}
