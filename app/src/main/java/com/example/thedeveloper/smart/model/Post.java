package com.example.thedeveloper.smart.model;


import java.util.HashMap;
import java.util.Map;

public class Post {
    private String authorName;
    private String status;
    private String supName;
    private String content;
    private String uid;
    private String pushKey;
    private int numberOfShares;
    private double counter;
    private double salary;

    public Map<String, Object> ToMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("authorName", authorName);
        result.put("status", status);
        result.put("supName", supName);
        result.put("content", content);
        result.put("uid", uid);
        result.put("pushKey", pushKey);
        result.put("numberOfShares", numberOfShares);
        result.put("counter", counter);
        result.put("salary", salary);

        return result;
    }

    public Post() {
    }

    public Post(String authorName, String status, String supName, String content,
                String uid, String pushKey, int numberOfShares, double counter) {
        this.authorName = authorName;
        this.status = status;
        this.supName = supName;
        this.content = content;
        this.uid = uid;
        this.pushKey = pushKey;
        this.numberOfShares = numberOfShares;
        this.counter = counter;
        if (status.equals("posted")) {
            this.salary = counter * numberOfShares;
        } else {
            if (numberOfShares == 0) {
                this.salary = counter;

            }  else {
            this.salary = counter * numberOfShares;
            }
        }

    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public int getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(int numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public double getCounter() {
        return counter;
    }

    public void setCounter(double counter) {
        this.counter = counter;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
