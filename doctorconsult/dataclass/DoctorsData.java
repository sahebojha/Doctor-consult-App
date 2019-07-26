package com.example.sahebojha.doctorconsult.dataclass;

public class DoctorsData {
    private String name, email, photo, address, specialist, phone, pin;
    private int id;
    private float rating, myRating;


    public DoctorsData(int id, String name, String email, String photo, String address, String specialist, String phone, String pin, float rating, float myRating) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.address = address;
        this.specialist = specialist;
        this.phone = phone;
        this.pin = pin;
        this.rating = rating;
        this.myRating = myRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getMyRating() {
        return myRating;
    }

    public void setMyRating(float myRating) {
        this.myRating = myRating;
    }
}
