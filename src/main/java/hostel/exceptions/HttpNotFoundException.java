package hostel.exceptions;

import org.springframework.http.HttpStatus;

public class HttpNotFoundException extends HttpException{
    public HttpNotFoundException(String resourceName) {
        super(HttpStatus.NOT_FOUND, "error.resource.not_found", new Object[]{resourceName});
    }
}
