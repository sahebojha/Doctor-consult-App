package com.example.sahebojha.doctorconsult.dataclass;

public class PlaceDetailsData {
    private int id, doctorId;
    private String placeName, placeAddress, placePin, placePhone;

    public PlaceDetailsData(int id, int doctorId, String placeName, String placeAddress, String placePin, String placePhone) {

        this.id = id;
        this.doctorId = doctorId;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.placePin = placePin;
        this.placePhone = placePhone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getPlacePin() {
        return placePin;
    }

    public void setPlacePin(String placePin) {
        this.placePin = placePin;
    }

    public String getPlacePhone() {
        return placePhone;
    }

    public void setPlacePhone(String placePhone) {
        this.placePhone = placePhone;
    }


}
