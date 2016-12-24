package com.waypointer.gpsfileconverter.parser.waypoint;

import com.waypointer.gpsfileconverter.GpsPoint;
import com.waypointer.gpsfileconverter.parser.AbstractKML22Parser;
import de.micromata.opengis.kml.v_2_2_0.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KML22Parser extends AbstractKML22Parser<GpsPoint> {

    protected List<GpsPoint> parseFeature(Feature feature) {
        List<GpsPoint> result = new ArrayList<>();

        if (feature != null) {
            parseFeatureInternal(feature, result);
        }
        for (GpsPoint waypoint : result) {
            if (waypoint.getCreatedDate() == null) {
                waypoint.setCreatedDate(new Date());
            }
        }
        return result;
    }

    private void parseFeatureInternal(Feature feature, List<GpsPoint> result) {
        if (feature instanceof Document) {
            Document document = (Document) feature;
            for (Feature currFeature : document.getFeature()) {
                result.addAll(parseFeature(currFeature));
            }
        } else if (feature instanceof Folder) {
            Folder folder = (Folder) feature;
            for (Feature currFeature : folder.getFeature()) {
                result.addAll(parseFeature(currFeature));
            }
        } else if (feature instanceof Placemark) {
            GpsPoint parsedPoint = parsePlacemark((Placemark) feature);
            if (parsedPoint != null) {
                result.add(parsedPoint);
            }
        }
    }

    private GpsPoint parsePlacemark(Placemark placemark) {
        GpsPoint wpt = null;

        Geometry geometry = placemark.getGeometry();
        if (geometry instanceof Point) {
            wpt = new GpsPoint();

            Point point = (Point) geometry;
            wpt.setName(placemark.getName());
            wpt.setDescription(placemark.getDescription());
            Coordinate coordinate = point.getCoordinates().get(0);
            if (coordinate != null) {
                wpt.setLatitude(coordinate.getLatitude());
                wpt.setLongitude(coordinate.getLongitude());
            }
        }

        return wpt;
    }

}
