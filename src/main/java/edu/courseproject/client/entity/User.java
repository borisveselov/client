package edu.courseproject.client.entity;

//import lombok.Builder;
//import lombok.Setter;
//
//@Setter
//@Builder
public class User {
    private long idUser;
    private String login;
    private String password;
    private String role;
    private String status;

    public User() {
    }

    public User(long idUser, String status) {
        this.idUser = idUser;
        this.status = status;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login, String password, String role, String status) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.status = status;
    }
}
