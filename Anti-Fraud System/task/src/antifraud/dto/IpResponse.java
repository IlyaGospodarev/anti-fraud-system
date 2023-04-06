package antifraud.dto;

import antifraud.model.IpAddress;

public class IpResponse {
    private Long id;
    private String ip;

    public IpResponse() {
    }

    public IpResponse(Long id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    public IpResponse(IpAddress ipAddress) {
        this.id = ipAddress.getId();
        this.ip = ipAddress.getIp();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
