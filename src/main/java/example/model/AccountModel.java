package example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class AccountModel {
    @NotEmpty
    private String userIdOrPhone;

    @NotEmpty
    private String bankIdOrNameOrPhone;
}
