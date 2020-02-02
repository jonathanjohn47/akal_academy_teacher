package com.rainbow.teacheriiy.ui.DailyMarks;

public class StudentDetails {
    private String student_name;
    private String student_Id;
    private String student_class;

    public StudentDetails(String student_name, String student_Id, String student_class) {
        this.student_name = student_name;
        this.student_Id = student_Id;
        this.student_class = student_class;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_Id() {
        return student_Id;
    }

    public void setStudent_Id(String student_Id) {
        this.student_Id = student_Id;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }
}

