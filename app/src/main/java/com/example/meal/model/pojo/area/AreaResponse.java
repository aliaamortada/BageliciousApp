package com.example.meal.model.pojo.area;

import java.util.List;

public class AreaResponse {
    private List<Area> areas; // "meals" is used in the actual API response

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> meals) {
        this.areas = areas;
    }
}
