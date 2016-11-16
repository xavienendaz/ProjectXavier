package com.example.xavier.projectxavier;

/**
 * Created by Xavier on 05.11.2016.
 */

public class User {

    private int id;
    private String username;
    private String password;
    private byte [] image;


    public User(){}

    public User(String username, String password){
        this.username=username;
        this.password=password;
    }

    public User(String username, byte [] image){
        this.username=username;
        this.image=image;
    }

    public User(int id, String username, String password, byte [] image){
        this.id=id;
        this.username=username;
        this.password=password;
        this.image=image;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public byte[] getImage() { return image; }

    public void setImage(byte[] image) { this.image = image; }

    public User(String username){
        this.username=username;
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


    public String toString() {
        return "User [id=" + id + ", username=" + username + " image: "+image+ "]";
    }
}

