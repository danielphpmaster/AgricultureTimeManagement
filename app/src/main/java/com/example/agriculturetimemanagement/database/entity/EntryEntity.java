package com.example.agriculturetimemanagement.database.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EntryEntity {

    private String id;

    private String name;

    private int category;

    private String time;

    public EntryEntity() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof EntryEntity)) return false;
        EntryEntity o = (EntryEntity) obj;
        return o.getId().equals(this.getId());
    }

    @Override
    public String toString() {
        return name + " " + category + " " + time;

    }

    //@Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("category", category);
        result.put("time", time);
        return result;
    }
}
