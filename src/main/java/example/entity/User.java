package example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "user_generator")
    private Integer id;

    @NotNull
    private String surname;

    @NotNull
    private String name;

    @NotNull
    private String patronymic;

    @NotNull
    private String address;

    @NotNull
    @Column(unique = true)
    private String phone;
}
