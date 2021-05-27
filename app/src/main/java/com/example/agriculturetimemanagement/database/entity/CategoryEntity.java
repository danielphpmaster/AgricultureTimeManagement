package com.example.agriculturetimemanagement.database.entity;

import java.util.HashMap;
import java.util.Map;

public class CategoryEntity {

    private String id;

    private String name;

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

    public CategoryEntity(){

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof CategoryEntity)) return false;
        CategoryEntity o = (CategoryEntity) obj;
        return o.getId().equals(this.getId());
    }

    @Override
    public String toString() { return name; }

    // @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        return result;
    }


}
