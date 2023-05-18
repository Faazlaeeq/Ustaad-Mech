package com.example.ustaadmech;

public class m_d_modal {
    String name,rating;

    public m_d_modal(String name, String rating) {
        this.name = name;
        this.rating = rating;
    }

    public m_d_modal() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
