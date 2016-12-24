package com.waypointer.gpsfileconverter.parser.waypoint;

import com.waypointer.gpsfileconverter.GpsPoint;
import com.waypointer.gpsfileconverter.parser.FileParser;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KML21ParserTest {


    @Test
    public void testFileParsing() throws Exception {
        String path = getClass().getResource("/parsing/sample_kml21.kml").getFile();
        FileInputStream fis = new FileInputStream(path);

        FileParser<GpsPoint> parser = new KML21Parser();
        List<GpsPoint> waypoints = parser.parseFile(new BufferedInputStream(fis));
        assertThat(waypoints).hasSize(1);
    }

    @Test
    public void testFileDetection() throws Exception {
        String path = getClass().getResource("/parsing/sample_kml21.kml").getFile();
        FileInputStream fis = new FileInputStream(path);

        BufferedInputStream bis = new BufferedInputStream(fis);
        FileParser<GpsPoint> parser = new KML21Parser();
        assertThat(parser.isParsableFile(bis)).isTrue();
    }

    @Test
    public void testIntegrity() throws Exception {
        String path = getClass().getResource("/parsing/sample_kml21.kml").getFile();
        FileInputStream fis = new FileInputStream(path);

        BufferedInputStream bis = new BufferedInputStream(fis);
        FileParser<GpsPoint> parser = new KML21Parser();
        assertThat(parser.isParsableFile(bis)).isTrue();
        List<GpsPoint> waypoints = parser.parseFile(bis);
        assertThat(waypoints).hasSize(1);
    }

}
