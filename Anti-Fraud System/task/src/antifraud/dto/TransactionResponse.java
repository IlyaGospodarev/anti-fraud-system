package antifraud.dto;

import antifraud.model.TransactionStatus;

public class TransactionResponse {
    private TransactionStatus result;

    private String info;

    public TransactionResponse() {
    }

    public TransactionResponse(TransactionStatus result, String info) {
        this.result = result;
        this.info = info;
    }

    public TransactionStatus getResult() {
        return result;
    }

    public void setResult(TransactionStatus result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
