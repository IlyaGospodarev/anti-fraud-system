package antifraud.dto;

import javax.validation.constraints.Min;

public class TransactionRequest {
    @Min(value = 1)
    private long amount;

    public TransactionRequest() {
    }

    public TransactionRequest(int amount) {
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
