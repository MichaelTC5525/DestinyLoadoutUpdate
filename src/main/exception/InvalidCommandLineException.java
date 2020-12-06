package main.exception;

public class InvalidCommandLineException extends RuntimeException {

    public InvalidCommandLineException(String msg) {
        super(msg);
    }
}
