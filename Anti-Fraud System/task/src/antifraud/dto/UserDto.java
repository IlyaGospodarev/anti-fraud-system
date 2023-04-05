package antifraud.dto;

import antifraud.model.User;

public class UserDto {

    private long id;

    private String name;

    private String username;

    public UserDto() {
    }

    public UserDto(long id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
