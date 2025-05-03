package io.mainia.model;

import java.io.IOException;

public class WrongFileFormatException extends IOException {
    public WrongFileFormatException() {super();}
    public WrongFileFormatException(String message) {super(message);}
    public WrongFileFormatException(Throwable cause) {super(cause);}
    public WrongFileFormatException(String message, Throwable cause) {super(message, cause);}
}
