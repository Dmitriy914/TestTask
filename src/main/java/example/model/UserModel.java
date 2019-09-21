package example.model;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class UserModel {
    @NotEmpty
    private String surname;

    @NotEmpty
    private String name;

    @NotEmpty
    private String patronymic;

    @NotEmpty
    private String address;

    @NotEmpty
    private String phone;
}
