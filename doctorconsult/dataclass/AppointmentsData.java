package com.example.sahebojha.doctorconsult.dataclass;

public class AppointmentsData {
    private int appId, srNo, placeId, status;
    private String time, weekday, date, placeName;

    public int getAppId() {
        return appId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setAppId(int appId) {
        this.appId = appId;

    }

    public int getSrNo() {
        return srNo;
    }

    public void setSrNo(int srNo) {
        this.srNo = srNo;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AppointmentsData(int appId, int srNo, int placeId, String placeName, int status, String time, String weekday, String date) {
        this.appId = appId;
        this.srNo = srNo;
        this.placeId = placeId;
        this.placeName = placeName;
        this.status = status;

        this.time = time;
        this.weekday = weekday;
        this.date = date;
    }
}
