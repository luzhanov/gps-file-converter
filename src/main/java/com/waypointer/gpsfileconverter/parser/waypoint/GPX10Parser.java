package com.waypointer.gpsfileconverter.parser.waypoint;

import com.topografix.gpx._1._0.Gpx;
import com.waypointer.gpsfileconverter.GpsPoint;
import com.waypointer.gpsfileconverter.parser.AbstractGPX10Parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

public class GPX10Parser extends AbstractGPX10Parser<GpsPoint> {

    public List<GpsPoint> readObjectsFromGpxType(Gpx gpxType) {
        ArrayList<GpsPoint> resultList = new ArrayList<>();

        List<Gpx.Wpt> waypointsList = gpxType.getWpt();

        for (Gpx.Wpt currWptType : waypointsList) {
            double latitude = currWptType.getLat().doubleValue();
            double longitude = currWptType.getLon().doubleValue();

            String extractedName = currWptType.getName();
            String extractedDesc = currWptType.getDesc();
            String extractedCmt = currWptType.getCmt();
            XMLGregorianCalendar xmlCalendar = currWptType.getTime();

            GpsPoint newPoint = createPoint(latitude, longitude, extractedName, extractedDesc, extractedCmt, xmlCalendar);

            resultList.add(newPoint);
        }

        //set created date
        for (GpsPoint waypoint : resultList) {
            if (waypoint.getCreatedDate() == null) {
                waypoint.setCreatedDate(new Date());
            }
        }

        return resultList;
    }
}
