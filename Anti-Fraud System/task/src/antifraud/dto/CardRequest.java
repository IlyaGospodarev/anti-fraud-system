package antifraud.dto;

import org.hibernate.validator.constraints.CreditCardNumber;

public class CardRequest {
    @CreditCardNumber
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
