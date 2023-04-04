package antifraud.dto;

public class TransactionDto {
    private String result;

    public TransactionDto() {
    }

    public TransactionDto(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
