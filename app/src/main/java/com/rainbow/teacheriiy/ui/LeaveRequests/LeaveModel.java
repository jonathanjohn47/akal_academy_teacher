package com.rainbow.teacheriiy.ui.LeaveRequests;

public class LeaveModel {
    private String name, clas, date, reason;

    public LeaveModel(String name, String clas, String date, String reason) {
        this.name = name;
        this.clas = clas;
        this.date = date;
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public String getClas() {
        return clas;
    }

    public String getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }
}
