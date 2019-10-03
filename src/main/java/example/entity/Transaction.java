package example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "transaction_generator")
    private Integer id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Account accountSend;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Account accountGet;

    @NotNull
    @Positive
    @Column(scale = 2)
    private BigDecimal amount;

    @NotNull
    private Instant instant;
}
