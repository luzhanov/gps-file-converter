package com.waypointer.gpsfileconverter.generator.waypoint;

import com.waypointer.gpsfileconverter.GpsPoint;
import com.waypointer.gpsfileconverter.generator.FileGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.waypointer.gpsfileconverter.FileConverterUtils.removeLineBreaks;

@Slf4j
public class WPTGenerator implements FileGenerator<GpsPoint> {

    @Override
    public void generateFile(OutputStream os, List<GpsPoint> objectList) {
        try {
            //todo: findbugs: reliance on default encoding
            os.write("OziExplorer Waypoint File Version 1.1\n".getBytes());
            os.write("WGS 84\n".getBytes());
            os.write("Reserved 2\n".getBytes());
            os.write("garmin\n".getBytes());

            int count = 1;
            for (GpsPoint waypoint : objectList) {
                StringBuilder record = new StringBuilder();
                Double longitude = new BigDecimal(waypoint.getLongitude()).setScale(6, RoundingMode.HALF_UP).doubleValue();
                Double latitude = new BigDecimal(waypoint.getLatitude()).setScale(6, RoundingMode.HALF_UP).doubleValue();
                record.append(count).append(", ")
                        .append(removeLineBreaks(waypoint.getName())).append(", ")
                        .append(latitude).append(", ")
                        .append(longitude).append(", ")
                        .append(", , , , , , ")
                        .append(removeLineBreaks(waypoint.getDescription()))
                        .append(", ")
                        .append(", , , , , , ,");
                os.write((record + "\n").getBytes());
                count++;
            }

            os.flush();
        } catch (IOException e) {
            logger.error("Error during WPT writing", e);
            throw new IllegalStateException("Error during generation of WPT file", e);
        }
    }

}
