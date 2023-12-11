package hostel.exceptions;

import org.springframework.http.HttpStatus;

public class HttpNoContentException extends HttpException{
    public HttpNoContentException() {
        super(HttpStatus.NO_CONTENT, null, null);
    }
}
