package com.example.sahebojha.doctorconsult.dataclass;

import com.example.sahebojha.doctorconsult.doctors.TimeDetails;

public class TimeDetailsData {
    private int id;
    private String weekday;
    private String time;

    public TimeDetailsData(int id, String weekday, String time) {
        this.id = id;
        this.weekday = weekday;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
