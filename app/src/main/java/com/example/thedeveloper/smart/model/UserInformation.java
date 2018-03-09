package com.example.thedeveloper.smart.model;

import java.util.HashMap;
import java.util.Map;

public class UserInformation {
    private String name;
    private String email;





    public UserInformation() {

    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);

        return result;
    }

    public UserInformation(String name, String email, String avatar, String key) {
        this.name = name;
        this.email = email;

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


}
