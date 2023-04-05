package antifraud.dto;

import javax.validation.constraints.NotBlank;

public class UnlockUserRequest {

    @NotBlank
    private String username;

    private String operation;

    public UnlockUserRequest() {
    }

    public UnlockUserRequest(String username, String operation) {
        this.username = username;
        this.operation = operation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
