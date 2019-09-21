package example.model;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
public class TransactionModel {
    @NotEmpty
    private String accountSendIdOrAccountNumber;

    @NotEmpty
    private String accountGetIdOrAccountNumber;

    @NotNull
    @Positive
    private BigDecimal amount;
}
