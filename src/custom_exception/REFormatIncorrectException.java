package custom_exception;

public class REFormatIncorrectException extends Exception{
    public REFormatIncorrectException(String info){
        super(info);
    }
}
