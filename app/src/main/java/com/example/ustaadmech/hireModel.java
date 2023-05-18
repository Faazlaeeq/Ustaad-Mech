package com.example.ustaadmech;

public class hireModel {
    String mechEmail,userEmail,problem,address,longi,lati,status,status_email;

    public hireModel() {
    }

    public String getStatus_email() {
        return status_email;
    }

    public void setStatus_email(String status_email) {
        this.status_email = status_email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public hireModel(String mechEmail, String userEmail, String problem, String address, String longi, String lati, String status, String status_email) {
        this.mechEmail = mechEmail;
        this.userEmail = userEmail;
        this.problem = problem;
        this.address = address;
        this.longi = longi;
        this.lati = lati;
        this.status = status;
        this.status_email = status_email;
    }

    public String getMechEmail() {
        return mechEmail;
    }

    public void setMechEmail(String mechEmail) {
        this.mechEmail = mechEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }
}
