package antifraud.dto;

import javax.validation.constraints.Pattern;

public class IpRequest {
    @Pattern(regexp = "^((\\d|[1-9]\\d|1\\d{2}|2[0-5]{2})\\.){3}(\\d|[1-9]\\d|1\\d{2}|2[0-5]{2})$")
    private String ip;

    public IpRequest() {
    }

    public IpRequest(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
