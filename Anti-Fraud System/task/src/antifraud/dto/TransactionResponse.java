package antifraud.dto;

public class TransactionResponse {
    private String result;

    public TransactionResponse() {
    }

    public TransactionResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
