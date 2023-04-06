package antifraud.dto;

import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class TransactionRequest {
    @Min(value = 1)
    private long amount;

    @Pattern(regexp = "^((\\d|[1-9]\\d|1\\d{2}|2[0-5]{2})\\.){3}(\\d|[1-9]\\d|1\\d{2}|2[0-5]{2})$")
    private String ip;

    @CreditCardNumber
    private String number;

    @NotEmpty
    private String region;

    @NotBlank
    private String date;

    public TransactionRequest() {
    }

    public TransactionRequest(int amount, String ip, String number, String region, String date) {
        this.amount = amount;
        this.ip = ip;
        this.number = number;
        this.region = region;
        this.date = date;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
