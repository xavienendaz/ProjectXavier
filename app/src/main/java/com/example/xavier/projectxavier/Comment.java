package com.example.xavier.projectxavier;

/**
 * Created by Xavier on 18.11.2016.
 */

public class Comment {


    private int id;
    private String content;
    private String username;
    private String date;
    private int id_question;

    public Comment(){}
    public Comment(int id, String content, String username, String date, int id_question){
        this.id=id;
        this.content=content;
        this.username=username;
        this.date=date;
        this.id_question=id_question;
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
