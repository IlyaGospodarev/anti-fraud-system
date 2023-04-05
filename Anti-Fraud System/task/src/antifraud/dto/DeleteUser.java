package antifraud.dto;

public class DeleteUser {
    private String username;
    private String status = "Deleted successfully!";

    public DeleteUser() {
    }

    public DeleteUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
