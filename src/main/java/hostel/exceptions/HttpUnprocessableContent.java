package hostel.exceptions;

import org.springframework.http.HttpStatus;
public class HttpUnprocessableContent extends HttpException{
    public HttpUnprocessableContent() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, null, new Object[]{});
    }
}
