package hostel.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class HttpExceptionHandler {

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<String> handleConflictException(HttpException httpException) {
        return new ResponseEntity<>(httpException.getMessageRef() != null ?
                messageSource.getMessage(httpException.getMessageRef(), httpException.getArguments(), Locale.getDefault()) : "",
                httpException.getHttpStatus());
    }
}
