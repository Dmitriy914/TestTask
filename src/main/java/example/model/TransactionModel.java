package example.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransactionModel {
    private String accountSend_IdOrAccountNumber;
    private String accountGet_IdOrAccountNumber;
    private BigDecimal amount;
}
