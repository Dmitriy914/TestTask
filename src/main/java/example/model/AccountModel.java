package example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountModel {
    private String userIdOrPhone;
    private String bankIdOrNameOrPhone;

    @Override
    public String toString(){
        return "{\"userIdOrPhone\":\"" + userIdOrPhone + "\",\"bankIdOrNameOrPhone\":\"" + bankIdOrNameOrPhone + "\"}";
    }
}
