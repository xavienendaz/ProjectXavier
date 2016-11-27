package com.example.xavier.projectxavier;

/**
 * Created by Xavier on 18.11.2016.
 */

public class Comment {


    private int id;
    private String content;
    private String date;
    private String username;
    private int id_question;
    private byte [] image;

    public Comment(){}


    public Comment(int id, String content, String date, String username, int id_question, byte[] image) {
        this.id=id;
        this.content=content;
        this.date=date;
        this.username=username;
        this.id_question=id_question;
        this.image=image;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId_question() {
        return id_question;
    }

    public void setId_question(int id_question) {
        this.id_question = id_question;
    }
}
