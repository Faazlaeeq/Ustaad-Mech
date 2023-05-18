package com.example.ustaadmech;

public class mechanic_data {
    String name,email,pass,cnic,phone,address,bike,tools,car_mech,bike_mech,individual,store,status,imgurl,time;
    public mechanic_data()
    {

    }
    public mechanic_data(String name, String email, String pass, String cnic, String phone, String address, String bike, String tools, String car_mech, String bike_mech, String individual, String store, String status,String imgurl,String time) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.cnic = cnic;
        this.phone = phone;
        this.address = address;
        this.bike = bike;
        this.tools = tools;
        this.car_mech = car_mech;
        this.bike_mech = bike_mech;
        this.individual = individual;
        this.store = store;
        this.status = status;
        this.imgurl = imgurl;
        this.time=time;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl)
    {
        this.imgurl = imgurl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBike() {
        return bike;
    }

    public void setBike(String bike) {
        this.bike = bike;
    }

    public String getTools() {
        return tools;
    }

    public void setTools(String tools) {
        this.tools = tools;
    }

    public String getCar_mech() {
        return car_mech;
    }

    public void setCar_mech(String car_mech) {
        this.car_mech = car_mech;
    }

    public String getBike_mech() {
        return bike_mech;
    }

    public void setBike_mech(String bike_mech) {
        this.bike_mech = bike_mech;
    }

    public String getIndividual() {
        return individual;
    }

    public void setIndividual(String individual) {
        this.individual = individual;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}