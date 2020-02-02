package com.rainbow.teacheriiy.ui.TeacherAttendance;

public class TeacherAttendanceModel {
    String date, attendancevalue;

    public TeacherAttendanceModel(String date, String attendancevalue) {
        this.date = date;
        this.attendancevalue = attendancevalue;
    }

    public String getDate() {
        return date;
    }

    public String getAttendancevalue() {
        return attendancevalue;
    }
}
