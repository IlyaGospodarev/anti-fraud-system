package antifraud.dto;

import antifraud.model.TransactionStatus;

public class FeedbackRequest {
    private Long transactionId;
    private TransactionStatus feedback;

    public FeedbackRequest() {}

    public FeedbackRequest(Long transactionId, TransactionStatus feedback) {
        this.transactionId = transactionId;
        this.feedback = feedback;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionStatus getFeedback() {
        return feedback;
    }

    public void setFeedback(TransactionStatus feedback) {
        this.feedback = feedback;
    }

}
