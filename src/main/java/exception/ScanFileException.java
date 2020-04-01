package exception;

public class ScanFileException extends RuntimeException {
    public ScanFileException(Exception e) {
        super(e);
    }
}
