package example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class BankModel {
    @NotEmpty
    private String name;

    @NotEmpty
    private String address;

    @NotEmpty
    private String phone;
}
