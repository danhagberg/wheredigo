package net.digitaltsunami.wheredigo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construct an instance with provided message indicating the resource not found.
     * @param message Message indicating which resource was not found.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
