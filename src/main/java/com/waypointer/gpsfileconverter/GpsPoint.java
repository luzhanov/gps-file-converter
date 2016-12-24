package com.waypointer.gpsfileconverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GpsPoint {

    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private String description;
    private Date createdDate;

    public GpsPoint(Long id, String name, Double latitude, Double longitude, String description) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }


}
