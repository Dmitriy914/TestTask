package example.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DuplicateException extends RuntimeException {
    private String errorField;
}
