package example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "user_generator")
    private Integer id;

    @NotNull
    @NotEmpty
    private String surname;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String patronymic;

    @NotNull
    @NotEmpty
    private String address;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String phone;
}
