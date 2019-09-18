package example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "bank_id"})})
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "account_generator")
    private Integer id;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String accountNumber;

    @ManyToOne(optional = false)
    private User user;

    @NotNull
    @PositiveOrZero
    @Column(scale = 2)
    private BigDecimal balance;

    @ManyToOne(optional = false)
    private Bank bank;
}
