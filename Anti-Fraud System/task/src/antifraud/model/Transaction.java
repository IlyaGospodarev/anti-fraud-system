package antifraud.model;

public class Transaction {

    private int amount;

    public Transaction() {
    }

    public Transaction(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
