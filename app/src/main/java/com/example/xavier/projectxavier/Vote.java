package com.example.xavier.projectxavier;

import android.view.ViewOutlineProvider;

/**
 * Created by Xavier on 20.11.2016.
 */

public class Vote {

    int id;
    String username;
    String vote;
    String id_question;

    public Vote() {
    }

    public Vote(int id, String vote, String username, String id_question) {
        this.id = id;
        this.username = username;
        this.vote = vote;
        this.id_question = id_question;
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

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getId_question() {
        return id_question;
    }

    public void setId_question(String id_question) {
        this.id_question = id_question;
    }
}
