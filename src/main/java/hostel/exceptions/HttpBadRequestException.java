package hostel.exceptions;

import org.springframework.http.HttpStatus;

public class HttpBadRequestException extends HttpException{
    public HttpBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, "error.bad_request", new Object[]{message});
    }
}
