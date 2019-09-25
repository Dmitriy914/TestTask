package example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TransactionModel {
    @NotEmpty
    private String accountSendIdOrAccountNumber;

    @NotEmpty
    private String accountGetIdOrAccountNumber;

    @NotNull
    @Positive
    private BigDecimal amount;
}
