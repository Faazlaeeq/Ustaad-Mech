package com.example.ustaadmech;

public class mechModel {
    String Name,Email,address, bike_mech, car_mech,cnic,individual,pass,phone,status,bike_available,time,date,tools,store,rating;
    String imgurl;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public mechModel() {
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getTools() {
        return tools;
    }

    public void setTools(String tools) {
        this.tools = tools;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public mechModel(String name, String time,String rating, String imgurl, String tools, String store, String date, String email, String address, String bike_mech, String car_mech, String cnic, String individual, String pass, String phone, String status, String bike_available) {

        Name = name;
        Email = email;
        this.time = time;
        this.rating = rating;
        this.store = store;
        this.tools = tools;
        this.imgurl = imgurl;
        this.date = date;
        this.address = address;
        this.bike_mech = bike_mech;
        this.car_mech = car_mech;
        this.cnic = cnic;
        this.individual = individual;
        this.pass = pass;
        this.phone = phone;
        this.status = status;
        this.bike_available = bike_available;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBike_mech() {
        return bike_mech;
    }

    public void setBike_mech(String bike_mech) {
        this.bike_mech = bike_mech;
    }

    public String getCar_mech() {
        return car_mech;
    }

    public void setCar_mech(String car_mech) {
        this.car_mech = car_mech;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getIndividual() {
        return individual;
    }

    public void setIndividual(String individual) {
        this.individual = individual;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBike_available() {
        return bike_available;
    }

    public void setBike_available(String bike_available) {
        this.bike_available = bike_available;
    }
}
