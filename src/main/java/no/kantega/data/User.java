package no.kantega.data;

public class User {
    public Integer id;
    public String name;
    public String email;
    public String phone;
    public String username;

    public User(){

    }

    public User(Integer id, String name, String email, String phone, String username) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.username = username;
    }
}
