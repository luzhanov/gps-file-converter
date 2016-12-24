package com.waypointer.gpsfileconverter.parser.route;

import com.waypointer.gpsfileconverter.GpsRoute;
import com.waypointer.gpsfileconverter.GpsRoutePoint;
import com.waypointer.gpsfileconverter.parser.AbstractKML22Parser;
import de.micromata.opengis.kml.v_2_2_0.*;

import java.util.ArrayList;
import java.util.List;

public class KML22RouteParser extends AbstractKML22Parser<GpsRoute> {

    protected List<GpsRoute> parseFeature(Feature feature) {
        List<GpsRoute> result = new ArrayList<>();

        if (feature != null) {
            parseFeatureInternal(feature, result);
        }

        return result;
    }

    private void parseFeatureInternal(Feature feature, List<GpsRoute> result) {
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
            GpsRoute parsedPoint = parsePlacemark((Placemark) feature);
            if (parsedPoint != null) {
                result.add(parsedPoint);
            }
        }
    }

    private GpsRoute parsePlacemark(Placemark placemark) {
        GpsRoute rt = null;

        List<GpsRoutePoint> points = parseGeometry(placemark.getGeometry());
        if (points != null && !points.isEmpty()) {
            rt = new GpsRoute();
            rt.setRouteName(placemark.getName());
            int order = 1;
            for (GpsRoutePoint point : points) {
                point.setOrder(order++);
            }
            rt.setPoints(points);
        }

        return rt;
    }

    private List<GpsRoutePoint> parseGeometry(Geometry geometry) {
        List<GpsRoutePoint> routePoints = new ArrayList<>();

        if (geometry instanceof LineString) {
            List<Coordinate> geometryList = ((LineString) geometry).getCoordinates();

            for (Coordinate coordinate : geometryList) {
                routePoints.add(new GpsRoutePoint(coordinate.getLatitude(), coordinate.getLongitude(), 1));
            }
        } else if (geometry instanceof MultiGeometry) {
            MultiGeometry multiGeo = (MultiGeometry) geometry;
            if (!multiGeo.getGeometry().isEmpty()) {
                for (Geometry geometryInner : multiGeo.getGeometry()) {
                    routePoints.addAll(parseGeometry(geometryInner));
                }
            }
        }

        return routePoints;
    }

}
