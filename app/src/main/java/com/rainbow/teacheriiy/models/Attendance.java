package com.rainbow.teacheriiy.models;

public class Attendance {
    private String student_name;
    private long student_admno;


    public Attendance(){

    }

    public Attendance(String student_name, long student_admno) {
        this.student_name = student_name;
        this.student_admno = student_admno;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public long getStudent_admno() {
        return student_admno;
    }

    public void setStudent_admno(long student_admno) {
        this.student_admno = student_admno;
    }
}