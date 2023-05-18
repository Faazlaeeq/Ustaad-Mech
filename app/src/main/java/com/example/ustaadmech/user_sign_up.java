package com.example.ustaadmech;

public class user_sign_up {
    String name,email,pass,imgurl,phone;
    Double longi,lati;

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public Double getLati() {
        return lati;
    }

    public void setLati(Double lati) {
        this.lati = lati;
    }

    public user_sign_up()
    {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public user_sign_up(String name, String email, String pass, String imgurl, String phone, Double longi, Double lati) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.imgurl = imgurl;
        this.phone = phone;
        this.longi = longi;
        this.lati = lati;
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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }


    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
