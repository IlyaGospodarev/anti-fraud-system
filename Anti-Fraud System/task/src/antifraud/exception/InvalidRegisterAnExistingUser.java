package antifraud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "This user exists.")
public class InvalidRegisterAnExistingUser extends RuntimeException{
}
