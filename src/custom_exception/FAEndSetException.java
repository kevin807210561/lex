package custom_exception;

public class FAEndSetException extends Exception {
    public FAEndSetException(){
        super("FA end set can not be empty.");
    }
}
