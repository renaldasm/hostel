package hostel.exceptions;

import org.springframework.http.HttpStatus;

public class HttpConflictException extends HttpException{
    public HttpConflictException(String resourceName) {
        super(HttpStatus.CONFLICT, "error.resource.conflict", new Object[]{resourceName});
    }
}
