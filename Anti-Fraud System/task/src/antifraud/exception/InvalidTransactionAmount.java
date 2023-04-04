package antifraud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The transaction amount must be greater than 0.")
public class InvalidTransactionAmount extends RuntimeException{
}
