package com.example.xavier.projectxavier;

/**
 * Created by Xavier on 14.11.2016.
 */

public class Favorite {

    int id;
    String username;
    String id_question;

    public Favorite() {
    }

    public Favorite(int id, String username, String id_question) {
        this.id = id;
        this.username = username;
        this.id_question = id_question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_question() {
        return id_question;
    }

    public void setId_question(String id_question) {
        this.id_question = id_question;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return "Question [id=" + id + ", username=" + username + ", id_question=" + id_question + "]";
    }
}
