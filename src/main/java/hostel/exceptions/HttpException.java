package hostel.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final Object[] arguments;
    private final String messageRef;

    public HttpException(HttpStatus httpStatus, String messageRef, Object[] arguments) {
        super();
        this.httpStatus = httpStatus;
        this.arguments = arguments;
        this.messageRef = messageRef;
    }
}
