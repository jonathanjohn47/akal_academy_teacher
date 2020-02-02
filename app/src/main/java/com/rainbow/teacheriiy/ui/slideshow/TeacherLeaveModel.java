package com.rainbow.teacheriiy.ui.slideshow;

public class TeacherLeaveModel {
    private String teacherName, teacherID, date, leaveReason;

    public TeacherLeaveModel(String teacherName, String teacherID, String date, String leaveReason) {
        this.teacherName = teacherName;
        this.teacherID = teacherID;
        this.date = date;
        this.leaveReason = leaveReason;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public String getDate() {
        return date;
    }

    public String getLeaveReason() {
        return leaveReason;
    }
}
