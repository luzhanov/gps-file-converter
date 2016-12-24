package com.waypointer.gpsfileconverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GpsRoutePoint {

    private Long id;
    private Double latitude;
    private Double longitude;
    private Integer order = 0;

    public GpsRoutePoint(Double latitude, Double longitude, Integer order) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.order = order;
    }

}
