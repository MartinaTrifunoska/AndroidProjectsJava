package com.example.rest.models;

public class Reservation {
    private String date;
    private String time;
    private String numberGuests;
    private String tablePreference;
    private String specialRequests;
    private String email;
    private String confirm;

    public Reservation(){}

    public Reservation(String date, String time, String numberGuests, String tablePreference, String email) {
        this.date = date;
        this.time = time;
        this.numberGuests = numberGuests;
        this.tablePreference = tablePreference;
        this.email = email;
        this.confirm = "NO";
    }

    public Reservation(String date, String time, String numberGuests, String tablePreference, String specialRequests, String email) {
        this.date = date;
        this.time = time;
        this.numberGuests = numberGuests;
        this.tablePreference = tablePreference;
        this.specialRequests = specialRequests;
        this.email = email;
        this.confirm = "NO";
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumberGuests() {
        return numberGuests;
    }

    public String getTablePreference() {
        return tablePreference;
    }

    public void setTablePreference(String tablePreference) {
        this.tablePreference = tablePreference;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public String getEmail() {
        return email;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNumberGuests(String numberGuests) {
        this.numberGuests = numberGuests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
