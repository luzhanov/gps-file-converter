package com.waypointer.gpsfileconverter.generator.waypoint;

import com.waypointer.gpsfileconverter.GpsPoint;
import com.waypointer.gpsfileconverter.generator.FileGenerator;
import de.micromata.opengis.kml.v_2_2_0.*;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
public class KML22Generator implements FileGenerator<GpsPoint> {

    @Override
    public void generateFile(OutputStream os, List<GpsPoint> objectList) {
        try {
            final Kml kml = new Kml();
            Document doc = kml.createAndSetDocument();
            doc.setName("Waypointer KML");
            Folder folder = doc.createAndAddFolder();
            folder.setName("Waypointer");

            for (GpsPoint wpt : objectList) {
                Placemark placemark = new Placemark();
                placemark.setName(wpt.getName());
                if (wpt.getDescription() != null) {
                    placemark.setDescription(wpt.getDescription());
                }
                Point point = new Point();
                Double longitude = new BigDecimal(wpt.getLongitude()).setScale(6, RoundingMode.HALF_UP).doubleValue();
                Double latitude = new BigDecimal(wpt.getLatitude()).setScale(6, RoundingMode.HALF_UP).doubleValue();
                point.addToCoordinates(longitude, latitude);
                placemark.setGeometry(point);
                folder.addToFeature(placemark);
            }

            kml.marshal(os);
        } catch (FileNotFoundException e) {
            logger.error("Error during KML22 marshalling", e);
            throw new IllegalStateException("Error during generation of KML 2.2 file", e);
        }
    }
}
