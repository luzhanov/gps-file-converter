package com.waypointer.gpsfileconverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GpsRoute {

    private Long id;
    private String routeName;
    private List<GpsRoutePoint> points = new ArrayList<>();

    public GpsRoute(String routeName, List<GpsRoutePoint> points) {
        this.routeName = routeName;
        this.points = points;
    }

}
