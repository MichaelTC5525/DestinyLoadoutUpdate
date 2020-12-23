package main.exception;

public class TableRowNotFoundException extends RuntimeException {

    public TableRowNotFoundException(String msg) {
        super(msg);
    }

}
