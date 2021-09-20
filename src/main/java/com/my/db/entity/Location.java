package com.my.db.entity;

import java.io.Serializable;
import java.util.Map;

public class Location implements Serializable {
    private long id;
    private String name;
    private Map<String, String> address;

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                '}';
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAddress() {
        return address;
    }

    public void setAddress(Map<String, String> address) {
        this.address = address;
    }
}
