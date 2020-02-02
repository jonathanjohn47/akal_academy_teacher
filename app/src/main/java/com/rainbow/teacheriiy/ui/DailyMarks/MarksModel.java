package com.rainbow.teacheriiy.ui.DailyMarks;

public class MarksModel {
    private String student_name;
    private String student_marks;
    private String student_class;
    private String maximum_marks;
    private String student_Id;
    private String date;
    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStudent_Id() {
        return student_Id;
    }

    public void setStudent_Id(String student_Id) {
        this.student_Id = student_Id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_marks() {
        return student_marks;
    }

    public void setStudent_marks(String student_marks) {
        this.student_marks = student_marks;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }

    public String getMaximum_marks() {
        return maximum_marks;
    }

    public void setMaximum_marks(String maximum_marks) {
        this.maximum_marks = maximum_marks;
    }
}
