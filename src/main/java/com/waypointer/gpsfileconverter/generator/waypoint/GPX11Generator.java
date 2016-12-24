package com.waypointer.gpsfileconverter.generator.waypoint;

import com.topografix.gpx._1._1.GpxType;
import com.topografix.gpx._1._1.ObjectFactory;
import com.topografix.gpx._1._1.WptType;
import com.waypointer.gpsfileconverter.GpsPoint;
import com.waypointer.gpsfileconverter.generator.AbstractGPXGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class GPX11Generator extends AbstractGPXGenerator<GpsPoint> {

    protected GpxType fill(List<GpsPoint> pointsList) {
        ObjectFactory factory = new ObjectFactory();
        GpxType resultGPX = factory.createGpxType();

        fillMetadata(factory, resultGPX);

        //fill points
        List<WptType> waypointsList = resultGPX.getWpt();

        for (GpsPoint currPoint : pointsList) {
            WptType waypoint = factory.createWptType();

            waypoint.setName(currPoint.getName());
            waypoint.setDesc(currPoint.getDescription());
            //todo: set type using point categories name
            //waypoint.setType();
            waypoint.setLat(new BigDecimal(currPoint.getLatitude()).setScale(6, RoundingMode.HALF_UP));
            waypoint.setLon(new BigDecimal(currPoint.getLongitude()).setScale(6, RoundingMode.HALF_UP));

            waypointsList.add(waypoint);
        }

        return resultGPX;
    }
}
