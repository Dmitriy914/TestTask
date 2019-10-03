package example.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmptyException extends RuntimeException {
    String ErrorObject;
}
