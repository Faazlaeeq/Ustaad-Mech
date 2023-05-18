package com.example.ustaadmech.ui;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class update_user_class {

    public String uid;
    public String name;
    public String cnic;
    public String address;
    public String contact_no;

    public update_user_class(String uid, String name, String cnic, String address, String contact_no) {
        this.uid = uid;
        this.name = name;
        this.cnic = cnic;
        this.address = address;
        this.contact_no = contact_no;
    }

    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public update_user_class() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }



    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("address", address);
        result.put("cnic", cnic);
        result.put("phone", contact_no);

        return result;
    }
}

