package com.waypointer.gpsfileconverter.parser.waypoint;

import com.google.earth.kml._2.*;
import com.waypointer.gpsfileconverter.GpsPoint;
import com.waypointer.gpsfileconverter.parser.AbstractKML20Parser;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.xml.bind.JAXBElement;

@Slf4j
public class KML20Parser extends AbstractKML20Parser<GpsPoint> {

    @Override
    protected List<GpsPoint> readObjectsFromKml20Type(Kml rootKml) {
        ArrayList<GpsPoint> resultList = new ArrayList<>();

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

    private void parseObject(List<GpsPoint> resultList, Object obj) {
        if (obj instanceof Folder) {
            Folder folder = (Folder) obj;
            for (Object child : folder.getDocumentOrFolderOrGroundOverlay()) {
                parseObject(resultList, child);
            }
        } else if (obj instanceof Placemark) {
            resultList.add(parseKML20Placemark((Placemark) obj));
        }
    }

    private GpsPoint parseKML20Placemark(Placemark placemark) {
        GpsPoint waypoint = new GpsPoint();
        waypoint.setCreatedDate(new Date());

        for (Object obj : placemark.getDescriptionOrNameOrSnippet()) {
            if (obj instanceof JAXBElement) {
                JAXBElement jaxbElement = (JAXBElement) obj;

                String name = jaxbElement.getName().getLocalPart();
                if ("name".equalsIgnoreCase(name)) {
                    waypoint.setName(jaxbElement.getValue().toString());
                } else if ("description".equalsIgnoreCase(name)) {
                    waypoint.setDescription(jaxbElement.getValue().toString());
                }
            } else if (obj instanceof Point) {
                Point point = (Point) obj;
                double[] lanLot = parseKML20Coordinates(point.getCoordinates());
                waypoint.setLatitude(lanLot[1]);
                waypoint.setLongitude(lanLot[0]);
            } else if (obj instanceof TimePeriod) {
                TimePeriod tp = (TimePeriod) obj;
                parseTimePeriod(waypoint, tp);
            }
        }

        return waypoint;
    }

    private void parseTimePeriod(GpsPoint waypoint, TimePeriod tp) {
        if (tp.getEnd() != null && tp.getEnd().getTimeInstant() != null
                && tp.getEnd().getTimeInstant().getTimePosition() != null) {
            String timeStr = tp.getEnd().getTimeInstant().getTimePosition();
            if (!timeStr.trim().isEmpty()) {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //2012-01-18T14:15:22Z
                try {
                    timeStr = timeStr.replace('T', ' ');
                    timeStr = timeStr.replace('Z', ' ');

                    format1.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date parsedDate = format1.parse(timeStr.trim());
                    waypoint.setCreatedDate(parsedDate);
                } catch (ParseException e) {
                    logger.error("Error during date parsing for KML 2.0");
                }
            }
        }
    }

    private double[] parseKML20Coordinates(String coordinateString) {
        double[] result = new double[2];

        String[] parts = coordinateString.split(",");
        result[0] = Double.parseDouble(parts[0]);
        result[1] = Double.parseDouble(parts[1]);

        return result;
    }

}
