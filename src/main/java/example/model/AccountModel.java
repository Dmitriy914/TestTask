package example.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class AccountModel {
    @NotEmpty
    private String userIdOrPhone;

    @NotEmpty
    private String bankIdOrNameOrPhone;

    @Override
    public String toString(){
        return "{\"userIdOrPhone\":\"" + userIdOrPhone + "\",\"bankIdOrNameOrPhone\":\"" + bankIdOrNameOrPhone + "\"}";
    }
}
