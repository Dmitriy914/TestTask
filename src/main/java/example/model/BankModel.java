package example.model;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class BankModel {
    @NotEmpty
    private String name;

    @NotEmpty
    private String address;

    @NotEmpty
    private String phone;
}
