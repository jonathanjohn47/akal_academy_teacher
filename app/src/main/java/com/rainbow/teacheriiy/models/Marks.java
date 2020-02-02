package com.rainbow.teacheriiy.models;

public class Marks {

    private String student_admno;
    private String student_name;
    private String subject;
    private String date;
    private String student_class;
    private String homework;
    private boolean isFinished;

    public Marks(String student_admno, String student_name, String subject, String date, String student_class, String homework, boolean isFinished) {
        this.student_admno = student_admno;
        this.student_name = student_name;
        this.subject = subject;
        this.date = date;
        this.student_class = student_class;
        this.homework = homework;
        this.isFinished = isFinished;
    }

    private Marks(){

    }

    public String getStudent_admno() {
        return student_admno;
    }

    public void setStudent_admno(String student_admno) {
        this.student_admno = student_admno;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
