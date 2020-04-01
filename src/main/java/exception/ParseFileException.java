package exception;

public class ParseFileException extends RuntimeException {
    public ParseFileException(Exception e) {
        super(e);
    }
}
