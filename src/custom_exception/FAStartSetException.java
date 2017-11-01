package custom_exception;

public class FAStartSetException extends Exception {
    public FAStartSetException(){
        super("FA start set can not be empty");
    }
}
