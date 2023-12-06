package ru.clevertec.exception;

public class PDFGenerationException extends RuntimeException {
    public PDFGenerationException() {
    }

    public PDFGenerationException(String message) {
        super(message);
    }

    public PDFGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PDFGenerationException(Throwable cause) {
        super(cause);
    }

    public PDFGenerationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
