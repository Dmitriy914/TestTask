package example.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransactionModel {
    private String accountSendIdOrAccountNumber;
    private String accountGetIdOrAccountNumber;
    private BigDecimal amount;
}
