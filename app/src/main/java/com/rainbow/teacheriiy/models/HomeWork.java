package com.rainbow.teacheriiy.models;

public class HomeWork {
    private String date;
    private String homework;
    private String subject;
    private String clas;

    public HomeWork(String date, String homework, String subject, String clas) {
        this.date = date;
        this.homework = homework;
        this.subject = subject;
        this.clas = clas;
    }

    public HomeWork(){

    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
