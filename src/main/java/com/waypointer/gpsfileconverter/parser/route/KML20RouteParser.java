package com.waypointer.gpsfileconverter.parser.route;

import com.google.earth.kml._2.Folder;
import com.google.earth.kml._2.Kml;
import com.google.earth.kml._2.LineString;
import com.google.earth.kml._2.Placemark;
import com.waypointer.gpsfileconverter.GpsRoute;
import com.waypointer.gpsfileconverter.GpsRoutePoint;
import com.waypointer.gpsfileconverter.parser.AbstractKML20Parser;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;

public class KML20RouteParser extends AbstractKML20Parser<GpsRoute> {

    @Override
    protected List<GpsRoute> readObjectsFromKml20Type(Kml rootKml) {
        ArrayList<GpsRoute> resultList = new ArrayList<>();

        if (rootKml.getDocument() != null && rootKml.getDocument().getDocumentOrFolderOrGroundOverlay() != null) {
            for (Object obj : rootKml.getDocument().getDocumentOrFolderOrGroundOverlay()) {
                parseObject(resultList, obj);
            }
        }

        if (rootKml.getFolder() != null && rootKml.getFolder().getDocumentOrFolderOrGroundOverlay() != null) {
            for (Object obj : rootKml.getFolder().getDocumentOrFolderOrGroundOverlay()) {
                parseObject(resultList, obj);
            }
        }

        return resultList;
    }

    private void parseObject(List<GpsRoute> resultList, Object obj) {
        if (obj instanceof Folder) {
            Folder folder = (Folder) obj;
            for (Object child : folder.getDocumentOrFolderOrGroundOverlay()) {
                parseObject(resultList, child);
            }
        } else if (obj instanceof Placemark) {
            resultList.add(parseKML20Placemark((Placemark) obj));
        }
    }

    private GpsRoute parseKML20Placemark(Placemark placemark) {
        GpsRoute rt = new GpsRoute();

        for (Object obj : placemark.getDescriptionOrNameOrSnippet()) {
            if (obj instanceof JAXBElement) {
                JAXBElement jaxbElement = (JAXBElement) obj;

                String name = jaxbElement.getName().getLocalPart();

                if ("name".equalsIgnoreCase(name)) {
                    rt.setRouteName(jaxbElement.getValue().toString());
                }
            } else if (obj instanceof LineString) {
                LineString lineStringElement = (LineString) obj;
                String coordinates = lineStringElement.getCoordinates();
                List<Coordinate> coordinateList = parseKML20Coordinates(coordinates);

                List<GpsRoutePoint> routePoints = new ArrayList<>();
                int order = 1;
                for (Coordinate coordinate : coordinateList) {
                    routePoints.add(new GpsRoutePoint(coordinate.getLatitude(), coordinate.getLongitude(), order));
                    order++;
                }

                rt.setPoints(routePoints);
            }
        }

        return rt;
    }

    private List<Coordinate> parseKML20Coordinates(String coordinateString) {
        List<Coordinate> result = new ArrayList<>();

        String[] parts = coordinateString.split("\\n");
        for (String coordinate : parts) {
            if (coordinate.contains(",") && coordinate.contains(".")) {
                String[] part = coordinate.split(",");
                result.add(new Coordinate(Double.parseDouble(part[0]), Double.parseDouble(part[1])));
            }
        }

        return result;
    }
}
