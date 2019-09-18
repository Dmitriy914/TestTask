package example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class Bank {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "bank_generator")
    private Integer id;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String name;

    @NotNull
    @NotEmpty
    private String address;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String phone;
}
