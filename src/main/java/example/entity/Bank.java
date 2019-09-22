package example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class Bank {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "bank_generator")
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private String address;

    @NotNull
    @Column(unique = true)
    private String phone;
}
