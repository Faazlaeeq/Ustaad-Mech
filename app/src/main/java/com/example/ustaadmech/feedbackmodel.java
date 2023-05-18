package com.example.ustaadmech;

public class feedbackmodel {
    String user_email,mech_email,msg,rating;
    public feedbackmodel() {
    }

    public feedbackmodel(String user_email, String mech_email, String msg, String rating) {
        this.user_email = user_email;
        this.mech_email = mech_email;
        this.msg = msg;
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getMech_email() {
        return mech_email;
    }

    public void setMech_email(String mech_email) {
        this.mech_email = mech_email;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
