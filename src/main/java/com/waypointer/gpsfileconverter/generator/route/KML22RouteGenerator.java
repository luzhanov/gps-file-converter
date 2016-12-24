package com.waypointer.gpsfileconverter.generator.route;

import com.waypointer.gpsfileconverter.GpsRoute;
import com.waypointer.gpsfileconverter.GpsRoutePoint;
import com.waypointer.gpsfileconverter.generator.FileGenerator;
import de.micromata.opengis.kml.v_2_2_0.*;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class KML22RouteGenerator implements FileGenerator<GpsRoute> {

    @Override
    public void generateFile(OutputStream os, List<GpsRoute> objectList) {
        try {
            final Kml kml = new Kml();
            Document doc = kml.createAndSetDocument();
            doc.setName("Waypointer KML");
            Folder folder = doc.createAndAddFolder();
            folder.setName("Waypointer");

            for (GpsRoute rt : objectList) {
                Placemark placemark = new Placemark();
                placemark.setName(rt.getRouteName() + " on Waypointer.info");

                LineString lineString = placemark.createAndSetLineString();
                List<Coordinate> coordinateList = new ArrayList<>();

                for (GpsRoutePoint rtp : rt.getPoints()) {
                    Double longitude = new BigDecimal(rtp.getLongitude()).setScale(6, RoundingMode.HALF_UP).doubleValue();
                    Double latitude = new BigDecimal(rtp.getLatitude()).setScale(6, RoundingMode.HALF_UP).doubleValue();
                    coordinateList.add(new Coordinate(longitude, latitude));
                }
                lineString.setCoordinates(coordinateList);
                placemark.setGeometry(lineString);
                folder.addToFeature(placemark);
            }

            kml.marshal(os);
        } catch (FileNotFoundException e) {
            logger.error("Error during KML22 marshalling.", e);
            throw new IllegalStateException("Error during generation of KML 2.2 file.", e);
        }
    }

}
