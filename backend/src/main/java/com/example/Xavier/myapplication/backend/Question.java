package com.example.Xavier.myapplication.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * Created by Xavier on 05.11.2016.
 */

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nkLike;
    private String topic;
    private String title;
    private String content;
    private String username;
    private byte [] image;
    private String date;
    String language;

    public Question(){

    }

    public Question(String topic, String title, String content, String username){
        this.topic=topic;
        this.title=title;
        this.content=content;
        this.username=username;
     }

    public Question(int id, String topic, String title, String content, String username){
        this.id=id;
        this.topic=topic;
        this.title=title;
        this.content=content;
        this.username=username;
    }

    public Question(int id, String topic, String title, String content, String username, byte [] image){
        this.id=id;
        this.topic=topic;
        this.title=title;
        this.content=content;
        this.username=username;
        this.image=image;
    }

    public Question(int id, String topic, String title, String content, String username, byte [] image, String nbLike, String date){
        this.id=id;
        this.topic=topic;
        this.title=title;
        this.content=content;
        this.username=username;
        this.image=image;
        this.nkLike=nbLike;
        this.date=date;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNkLike() {
        return nkLike;
    }

    public void setNkLike(String nkLike) {
        this.nkLike = nkLike;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Question(String title){
        this.title=title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String toString() {
        return "Question [id=" + id + ", topic=" + topic + ", title=" + title + ", content="+content+", uname="+ username
                + "image: "+image+ "]";
    }

}
