package cn.bulletjet.headline.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column(unique = true)
    private String username;
    @Column
    private String password;
    @Column
    private String salt;
    @Column(name = "head_url")
    private String headUrl;
    @OneToMany(targetEntity = News.class, mappedBy = "user", fetch = FetchType.LAZY)
    private List<News> news;


    public User() {
    }

    public User(String username) {
        this.username = username;
        this.password = "";
        this.salt = "";
        this.headUrl = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }


}
